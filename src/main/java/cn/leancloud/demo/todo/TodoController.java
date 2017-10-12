package cn.leancloud.demo.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUtils;

@Controller
@RequestMapping("/todos")
public class TodoController {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	ModelAndView listTodo(HttpServletRequest req, HttpServletResponse res) throws AVException {
		String offsetParam = req.getParameter("offset");
		int offset = 0;
		if (!AVUtils.isBlankString(offsetParam)) {
			offset = Integer.parseInt(offsetParam);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		List<Todo> data = new ArrayList<>();
		try {
			data = AVCloud.rpcFunction("list", params);
			req.setAttribute("todos", data);

		} catch (AVException e) {
			e.printStackTrace();
		}
		return new ModelAndView("todos/list", "todos", data);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	ModelAndView saveTodo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("content");

		try {
			AVObject note = new Todo();
			note.put("content", content);
			note.save();
		} catch (AVException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/todos/");
	}

}
