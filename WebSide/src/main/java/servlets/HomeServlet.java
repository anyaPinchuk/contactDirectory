package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/hi")
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Employee> empList = new ArrayList<Employee>();
//        Employee emp1 = new Employee();
//        emp1.setId(1);
//        emp1.setName("Pankaj");
//        emp1.setRole("Developer");
//        Employee emp2 = new Employee();
//        emp2.setId(2);
//        emp2.setName("Meghna");
//        emp2.setRole("Manager");
//        empList.add(emp1);
//        empList.add(emp2);
//        request.setAttribute("empList", empList);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/views/main/main.jspx");
        rd.forward(request, response);
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
