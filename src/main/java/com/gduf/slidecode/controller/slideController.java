package com.gduf.slidecode.controller;

import com.alibaba.fastjson.JSON;
import com.gduf.slidecode.utils.editphoto;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.Map;

/**
 * @Author 晨边#CB
 * @Date:created in  2019/8/24 15:30
 * @Version V1.0
 **/
@Controller
@RequestMapping("/slideCode")
public class slideController extends HttpServlet {


    @RequestMapping(value = "/slide")
    public void initslideCode(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException{
        String imagename = request.getParameter("imaname");

        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) {
            path = new File("");
        }

        editphoto.init(path.getPath());

        if(!StringUtils.isEmpty(imagename)){
            imagename = imagename.substring(imagename.lastIndexOf("/")+1,imagename.lastIndexOf("png")+3);
        }

        PrintWriter out = null;
        try{
            editphoto resourImg =new editphoto();
            Map<String,String> result = resourImg.create(request,imagename);
            out = response.getWriter();
            response.setContentType("application/json-rpc;charset=UTF-8");
            out.println(JSON.toJSONString(result));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (out!=null){
                out.close();
            }
        }

    }


    @PostMapping(value = "/checkServlet")
    public void checkCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String point = request.getParameter("point");
        Integer location_x = (Integer) request.getSession().getAttribute("location_x");
        HttpSession session = request.getSession();
        if ((Integer.valueOf(point) < location_x + 4) && (Integer.valueOf(point) > location_x - 4)) {
            //说明验证通过，
            session.setAttribute("status","验证成功");
//            session.setAttribute();
            outData(response, "success");
            System.out.println("验证通过");
        } else {
            session.setAttribute("status","验证失败");
            outData(response, "error");
            System.out.println("验证失败");
        }
    }

    private void outData(HttpServletResponse response, Object data) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            out = response.getWriter();
            response.setContentType("application/json-rpc;charset=UTF-8");
            out.println(JSON.toJSONString(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }


}
