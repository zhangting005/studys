package com.sl.jms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sl.entity.QueueTable;
import com.sl.jms.service.QueueTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping
@Controller
public class QueueTableController {

    @Autowired
    private QueueTableService queueTableService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String indexController(HttpServletRequest request) {
        List<QueueTable> queueTableList = queueTableService.queryAll();
        request.getSession().setAttribute("queueTableList", queueTableList);
        return "index";
    }

    @RequestMapping(value = "/getQueueTable", method = RequestMethod.GET)
    @ResponseBody
    public List<QueueTable> GetQueueTableController() {
        List<QueueTable> queueTableList = queueTableService.queryAll();
        return queueTableList;
    }

    @RequestMapping(value = "/clearMessage", method = RequestMethod.GET)
    public String clearMessage() {
        queueTableService.clearMessage();
        return "index";
    }
}
