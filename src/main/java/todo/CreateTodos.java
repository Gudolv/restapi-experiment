package todo;

import com.google.gson.Gson;
import spark.Request;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CreateTodos {

    private final EntityManagerFactory factory;
    private final EntityManager em;

    public CreateTodos(EntityManager em, EntityManagerFactory factory) {
        this.em = em;
        this.factory = factory;
    }

    public String CreateTodo(Request req) {
        Gson gson = new Gson();
        Todo todo = gson.fromJson(req.body(), Todo.class);
        em.getTransaction().begin();
        em.persist(todo);
        em.getTransaction().commit();
        return gson.toJson(todo);
    }

    public String AllTodos() {
        Gson gson = new Gson();
        List todos = em.createQuery("select todo from Todo todo").getResultList();
        return gson.toJson(todos);
    }

    public String getTodoById(String id) {
        Gson gson = new Gson();
        Todo todo = (Todo) em.createQuery("select todo from Todo todo where todo.id="+id).getSingleResult();
        return gson.toJson(todo);
    }

    public String removeTodos() {
        List todos =  em.createQuery("select todo from Todo todo").getResultList();
        for (Object todo : todos){
            em.getTransaction().begin();
            em.remove(todo);
            em.getTransaction().commit();
        }
        return ("Deleted all todos");

    }

    public String removeTodo(String id){
        Todo todo = (Todo) em.createQuery("select todo from Todo todo where todo.id=" +id).getSingleResult();
        em.getTransaction().begin();
        em.remove(todo);
        em.getTransaction().commit();
        return ("Deleted " + id + " from the todos");
    }

    public Object UpdateTodo(String id, Request req) {

        Todo todo = (Todo) em.createQuery("select todo from Todo todo where todo.id=" + id).getSingleResult();
        Gson gson = new Gson();
        Todo todo2 = gson.fromJson(req.body(), Todo.class);
        todo.setSummary(todo2.getSummary());;
        todo.setDescription(todo2.getSummary());
        em.getTransaction().begin();
        em.flush();
        em.getTransaction().commit();
        return gson.toJson(todo);
    }
}


