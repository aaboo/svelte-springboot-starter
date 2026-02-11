package com.aaboo.svelteSpringbootStarter.jobproc;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint("/socket/jobproc")
@EnableScheduling
public class JobprocSocketController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	@Autowired JobprocService jobprocService;
	@Autowired Gson gson;
	
	private String jobProcRunSaved = null;
    
    @OnOpen
    public void onOpen(Session session) throws Exception{
    	logger.info("open session : {}, clients = {}", session.toString(), clients);
    	if(!clients.contains(session)) {
    		clients.add(session);
    		logger.info("socket session [jobproc] open : {}", session);
    	}else {
    		logger.info("이미 연결된 session");
    	}
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    	logger.info("receive message : {}", message);
    	for(Session s : clients){
    		logger.info("send data : {}", message);
    		s.getBasicRemote().sendText(message);
    	}
    }
    
    @OnClose
    public void onClose(Session session) {
    	logger.info("socket session [jobproc] close : {}", session);
    	clients.remove(session);
    }
    
    @Scheduled(fixedDelay=3000)//3초마다 실행
    public void run() throws Exception {
    	if(!clients.isEmpty()) {
    		Map<String, Object> jobProcRunning = jobprocService.selectRunning();
    		logger.info("JobProcRunning(JobprocMapper.selectJob,selectProc) every 3s - client({})", clients.size());
    		String jobProcRun = gson.toJson(jobProcRunning);
    		//System.out.println("test333");
    		if(!jobProcRun.equals(jobProcRunSaved)) {
    			jobProcRunSaved = jobProcRun;
    			for(Session s : clients){        		
    				s.getBasicRemote().sendText(jobProcRunSaved);
    			}
    		}
    	}
    }
}
