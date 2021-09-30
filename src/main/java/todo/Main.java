package todo;

import com.google.gson.Gson;
import spark.Filter;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import static spark.Spark.*;


public class Main {
    private static final String PERSISTENCE_UNIT_NAME = "todos";
    private static CreateTodos ctodo;

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        ctodo = new CreateTodos(em, factory);

        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        // read the existing entries and write to console
        after((Filter) (req, res) -> {
            res.type("application/json");
        });

        get("/todos", (req,res) -> ctodo.AllTodos());

        get("/todos/:id", (req,res) -> {
            String id = req.params((":id"));
            try {
                return ctodo.getTodoById(id);
            }catch (NoResultException e){
                res.status(404);
                return ("Todo does not exist");
            }
        });
        post("/todos", (req,res) -> ctodo.CreateTodo(req));

        put("/todos/:id", (req,res) -> {
            String id = req.params(":id");
            req.queryParams("summary");
            req.queryParams("description");
            try {
                return ctodo.UpdateTodo(id,req);
            }catch (NoResultException e){
                res.status(404);
                return ("Todo does not exist");
            }


        });

        delete("/todos",(req,res) -> ctodo.removeTodos());

        delete("/todos/:id", (req,res) -> {
            String id = req.params(":id");
            try{
                return ctodo.removeTodo(id);
            }catch (NoResultException e){
                res.status(404);
                return ("Todo does not exist");
            }
        });
    }
}