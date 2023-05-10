package controller;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.User;
import dao.UserDao2;
import services.Services1;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/UserController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println(action);
		if (action.equalsIgnoreCase("register")) {
			User u = new User();
			u.setName(request.getParameter("name"));
			u.setContact(Long.parseLong(request.getParameter("contact")));
			u.setAddress(request.getParameter("address"));
			u.setEmail(request.getParameter("email"));
			u.setPassword(request.getParameter("password"));
			System.out.println(u);
			new UserDao2().insertUser(u);
			request.setAttribute("msg", "data inserted");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		
		else if (action.equalsIgnoreCase("login")) {
			User u = new User();
			u.setEmail(request.getParameter("email"));
			u.setPassword(request.getParameter("password"));
			System.out.println(u);
			User u1 = new UserDao2().doLogin(u);
			System.out.println(u1);
			if (u1 == null){
				request.setAttribute("msg", "email or password is incorrect");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			} 
			else {
				HttpSession session = request.getSession();
				session.setAttribute("data", u1);
				System.out.println("login done");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		}
		else if(action.equalsIgnoreCase("edit")) {
			int id = Integer.parseInt(request.getParameter("id"));
			User u = new User();
			request.setAttribute("data", u);
			request.getRequestDispatcher("update.jsp").forward(request, response);
			
		}
		else if(action.equalsIgnoreCase("update")) {
			User u = new User();
			u.setId(Integer.parseInt(request.getParameter("id")));
			u.setName(request.getParameter("name"));
			u.setContact(Integer.parseInt(request.getParameter("contact")));
			u.setAddress(request.getParameter("address"));
			u.setEmail(request.getParameter("email"));
			u.setPassword(request.getParameter("password"));
			User u1 = new UserDao2().updateUser(u);
			HttpSession session = request.getSession();
			session.setAttribute("data", u1);
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
		else if(action.equalsIgnoreCase("delete")) {
			int id = Integer.parseInt(request.getParameter("id"));
			User u = new UserDao2().deleteUser(id);
			response.sendRedirect("home.jsp");
		}
		else if (action.equalsIgnoreCase("change password")) {
			int id = Integer.parseInt(request.getParameter("id"));
			String op = request.getParameter("op");
			String np = request.getParameter("np");
			String cnp = request.getParameter("cnp");
			boolean flag = new UserDao2().checkOlPassword(id, op);
			System.out.println(flag);
			  if (flag == true) { 
				  if (np.equals(cnp))
				  { 
					  new UserDao2().udpatePassword(np, id);
					  response.sendRedirect("index.jsp");
				  } 
			  else {
				  request.setAttribute("msg1","new password and confirm new password not matched");
				  request.getRequestDispatcher("change-password.jsp").forward(request,response); 
			  	}
			  } 
			  else { 
				  request.setAttribute("msg","old password is not correct");
	
				  request.getRequestDispatcher("change-password.jsp").forward(request,response); 
			  }
			  System.out.println(op);
			  System.out.println(np);
			  System.out.println(cnp);
		}
		else if (action.equalsIgnoreCase("get otp")) {
			String email = request.getParameter("email");
			System.out.println(email);
			boolean flag = new UserDao2().checkEmail(email);
			System.out.println("------->"+flag);
			 if (flag == true) {
				Services1 s = new Services1();
				Random r = new Random();
				int num = r.nextInt(9999);
				System.out.println(num);
				s.sendMail(email, num);
				System.out.println(email+num);
				request.setAttribute("email", email);
				request.setAttribute("otp", num);
				request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
			}
			 else {
					request.setAttribute("msg", "email id not registerd");
					request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
				}
		}
		
	}
}
