package ma.emsi.tp4.controller;

import ma.emsi.tp4.dao.StudentDao;
import ma.emsi.tp4.model.Student;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "StudentServlet", value = "/")
public class StudentServlet extends HttpServlet {
    private StudentDao studentDao;
    public void init() {
        studentDao = new StudentDao();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertStudent(request, response);
                    break;
                case "/search":
                    searchStudent(request, response);
                    break;
                case "/delete":
                    deleteStudent(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateStudent(request, response);
                    break;
                default:
                    listStudent(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void searchStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        List<Student> listStudent = studentDao.selectStudents(name);
        request.setAttribute("listStudent", listStudent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/studentList.jsp");
        dispatcher.forward(request, response);
    }

    private void listStudent(HttpServletRequest request,
                             HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Student> listStudent = studentDao.selectAllStudents();
        request.setAttribute("listStudent", listStudent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/studentList.jsp");
        dispatcher.forward(request, response);
    }
    private void showNewForm(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("view/studentForm.jsp");
        dispatcher.forward(request, response);
    }
    private void showEditForm(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDao.selectStudent(id);
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("view/studentForm.jsp");
        request.setAttribute("student", existingStudent);
        dispatcher.forward(request, response);
    }
    private void insertStudent(HttpServletRequest request,
                               HttpServletResponse response)
            throws SQLException, IOException {
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String student_id = request.getParameter("student_id");
        String school = request.getParameter("school");
        String study_option = request.getParameter("study_option");
        String registration_year =
                request.getParameter("registration_year");
        Student newStudent = new Student(first_name, last_name,
                student_id, school, study_option, registration_year);
        studentDao.insertStudent(newStudent);
        response.sendRedirect("list");
    }
    private void updateStudent(HttpServletRequest request,
                               HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String student_id = request.getParameter("student_id");
        String school = request.getParameter("school");
        String study_option = request.getParameter("study_option");
        String registration_year = request.getParameter("registration_year");
        Student s1 = new Student(id, first_name, last_name, student_id,
                school, study_option, registration_year);
        studentDao.updateStudent(s1);
        response.sendRedirect("list");
    }
    private void deleteStudent(HttpServletRequest request,
                               HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        studentDao.deleteStudent(id);
        response.sendRedirect("list");
    }

}
