package study.projects.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import study.projects.entity.User;


@CrossOrigin
@RestController
@RequestMapping(path="/sharesteer")
public class LoginController {
	
	@Autowired
	JdbcTemplate temp;
	
	@PostMapping("/signup")
	public String f3(@RequestBody User u)
	{
		temp.update("insert into user values(?,?,?,?,?,?,?,?,?,?,?,?,?)","default",u.getUser_name(),u.getPassword(),u.getFirst_name(),
				u.getLast_name(),u.getBirth_date(),u.getEmail_id(),u.getContact(),u.getAadhar_image(),u.getUser_image(),
				u.getDriving_licence(),u.isIs_verified());
		
		return "User added successfully";
	}

	@GetMapping("/login")
	public String f4(@PathVariable String username,@PathVariable String password)
	{
		String messege="";
		try
		{
		User user=temp.queryForObject("select * from user where uname ="+username+"and password="+password, (rs,rownum)->{return new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getBlob(9),rs.getBlob(10),rs.getBlob(11),rs.getBoolean(12));});
		
		if(user!=null)
			messege= "Login Successful";
		else
			messege= "Password Invalid";
		
		}
		catch(Exception e)
		{
			messege= "No user exists";
		}
		return messege;
	}
	
	@GetMapping("/getDetail")
	public User f5(@PathVariable int id)
	{
		User user=temp.queryForObject("select * from user where user_id="+id, (rs,rownum)->{return new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getBlob(9),rs.getBlob(10),rs.getBlob(11),rs.getBoolean(12));});
		return user;
	}
	
	@GetMapping("/getAllUsers")
	public ArrayList<User> f6()
	{
		List<User> list=temp.query("select * from user",  (rs,rownum)->{return new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getBlob(9),rs.getBlob(10),rs.getBlob(11),rs.getBoolean(12));});
		return (ArrayList<User>) list;
	}

}
