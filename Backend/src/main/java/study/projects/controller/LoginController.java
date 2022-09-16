package study.projects.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import study.projects.entity.User;


@CrossOrigin
@RestController
@RequestMapping(path="/sharesteer")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class LoginController {

	@Autowired
	JdbcTemplate temp;

	@PostMapping("/signup")
	public String f3(@RequestBody User u)
	{
		String messege="";
		if(u==null)
			return "Enter Valid Data";
		try {
			temp.update("insert into user (user_name,password,first_name,last_name,birth_date,gender,email_id,contact,is_verified) values(?,?,?,?,?,?,?,?,?)",u.getUser_name(),u.getPassword(),u.getFirst_name(),
					u.getLast_name(),u.getBirth_date(),u.getGender(),u.getEmail_id(),u.getContact(),u.isIs_varified());
			messege= "User added successfully";
		} catch (DataAccessException e) {
			messege="Query Failed";
			e.printStackTrace();
		}
		int s=uploadPhoto(u.getUser_image(), u.getUser_id());
		return messege+s;
	}


	@GetMapping("/getDetail/{username}")
	public User f5(@PathVariable String username)
	{
		User user=null;
		try {
			user = temp.queryForObject("select * from user where user_name="+username, (rs,rownum)->{return new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return user;
	}

	
	@GetMapping("/getdetailsbyid/{user_id}")
	public User f6(@PathVariable int user_id)
	{
		User user=null;
		try {
			user = temp.queryForObject("select * from user where user_id="+user_id, (rs,rownum)->{return new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	@GetMapping("/getAllUsers")
	public ArrayList<User> f6()
	{
		List<User> list=new ArrayList<User>();
		try {
			list = temp.query("select * from user",(rs,rownum)->{return new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return (ArrayList<User>) list;
	}


	@PostMapping("/updateprofile")
	public String f4(@RequestBody User u)
	{
		String messege="";
		if(u==null)
			return "Enter Valid data";
		try {
			temp.update("update user set password='"+u.getPassword()+"',first_name='"+u.getFirst_name()+"', last_name='"+u.getLast_name()+" ',birth_date='"+u.getBirth_date()+"',gender='"+u.getGender()+"',contact='"+u.getContact()+"' where user_name= '"+u.getUser_name()+"'");
			messege="User Update successfully";

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			messege="Query Failed"+e.getMessage();

			e.printStackTrace();
		}

		return messege;
	}

	@PostMapping("/forgetpassword")
	public String f5(@RequestBody User u)
	{
		String messege="";
		if(u==null)
			return "Enter Valid data";
		try {
			temp.update("update user set password='"+u.getPassword()+"' where user_name='"+u.getUser_name()+"' and email_id='"+u.getEmail_id()+"'");
			messege="Password Changed Successfully";

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			messege="Query Failed"+e.getMessage();

			e.printStackTrace();
		}

		return messege;
	}
	
	@GetMapping("/getImage/{id}")
	public void getPhotoById(@PathVariable int id) {
//		String query = "select user_image from user where user_id=15";
//		Blob photo = temp.queryForObject(query, Blob.class);
//		return photo;
		
//		obj.setDriverClassName("com.mysql.cj.jdbc.Driver");
		String url="jdbc:mysql://localhost:3306/dotnetproject";
		String Username="root";
		String Password="C@stleking786";
		
		Connection con=null;
		PreparedStatement ps=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,Username,Password);
			File file=new File("D:\\IET_JAVA_Script\\Z2_FrontEnd\\share-steer\\src\\pages\\profile\\images\\user.jpg");
			FileOutputStream fos=new FileOutputStream(file);
			byte b[];
			Blob blob;
			
			ps=con.prepareStatement("select user_image from user where user_id="+id);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				blob=rs.getBlob(1);
				b=blob.getBytes(1,(int)blob.length());
				fos.write(b);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally {
			try {
				ps.close();
				con.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int uploadPhoto(String path,int id)
	{
		int status=0;
		String url="jdbc:mysql://localhost:3306/dotnetproject";
		String Username="root";
		String Password="C@stleking786";
		
		Connection con=null;
		PreparedStatement ps=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,Username,Password);
			File file=new File(path);
			FileInputStream fin=new FileInputStream(file);			
			ps=con.prepareStatement("update user set user_image=? where user_id="+id);
			ps.setBinaryStream(1, fin,(int)file.length());
			status=ps.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally {
			try {
				ps.close();
				con.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;
		
	}

}
