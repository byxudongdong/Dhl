package com.gson;

import java.util.ArrayList;  
import java.util.Date;  
import java.util.List;  
  
import com.google.gson.Gson;  
import com.google.gson.reflect.TypeToken;

import android.renderscript.Sampler.Value;
import android.util.Log;

public class SendJson{

   public static void main(String ref_id,  
						    String user_id,  
						    String task_time,						     
						    String task_name, 
						    String task_event,   						   
						    String doc_id,
						    String task_id,						   
						    String loc_id,
						    String box_id,
						    String sku,
						    String qty,
						    String last_opt_id,
						    int pushstate) 
    {  
        Gson gson = new Gson();  
  
        Task task1 = new Task(); 
        
        task1.setRefId(ref_id);  //13��
        task1.setUserId(user_id);
        task1.setTaskTime(task_time); 
        task1.setTaskName(task_name);  
        if(task_event == null)
        {
        	task1.setTaskEvent("");     
        }else {
        	task1.setTaskEvent(task_event); 
		}
        if(doc_id.equals("0"))
        {
        	task1.setDocId("");
        }else {
        	task1.setDocId(doc_id);
		}
        if(task_id.equals("0"))
        {
        	task1.setTaskId("");
        }else {
        	task1.setTaskId(task_id);
		}
        if(loc_id == null)
        {
        	task1.setLocId("");
        }else {
			task1.setLocId(loc_id);
		}
        
        if(box_id == null)
        {
        	task1.setBoxId("");
        }else {
			task1.setBoxId(box_id);
		}
        if(sku == null)
        {
        	task1.setSku("");
        }else{
        	task1.setSku(sku);
        }
        if(qty.equals("0"))
        {
        	task1.setQty("");
        }else{
        	task1.setQty(qty);
        }
        if(Integer.parseInt(ref_id) > 1)
        {
        	task1.setLastOptId(String.valueOf(Integer.parseInt(ref_id)-1));
        }else {
        	task1.setLastOptId("");
		}
        
        //task1.setPushState(pushstate);       
  
        // //////////////////////////////////////////////////////////  
        System.out.println("----------�򵥶���֮���ת��-------------");  
        // �򵥵�beanתΪjson  
        String s1 = gson.toJson(task1);  
//        s1 = "{"
//        		+"ref_id" + "\"1\","
//        		+"}";
        System.out.println("��Beanת��ΪJson===" + s1);  
  
        // jsonתΪ��Bean  
//        Task student = gson.fromJson(s1, Task.class);  
//        System.out.println("JsonתΪ��Bean===" + student);  

  
//        Task task2 = new Task();  
//        task2.setRefId(2);  
//        task2.setTaskName("����");  
//        task2.setTaskTime(new Date());  
//  
//        Task task3 = new Task();  
//        task3.setRefId(3);  
//        task3.setTaskName("����");  
//        task3.setTaskTime(new Date()); 
//  
//        List<Task> list = new ArrayList<Task>();  
//        list.add(task1);  
//        list.add(task2);  
//        list.add(task3);  
//  
//        System.out.println("----------�����͵�List֮���ת��-------------");  
//        // �����͵�listת��Ϊjson  
//        String s2 = gson.toJson(list);  
//        System.out.println("�����͵�listת��Ϊjson==" + s2);  
//  
//        // jsonתΪ�����͵�list  
//        List<Task> retList = gson.fromJson(s2,  
//                new TypeToken<List<Task>>() {  
//                }.getType());  
//        for (Task stu : retList) {  
//            System.out.println(stu);  
//        }
    }
	
	public SendJson() {
		// TODO �Զ����ɵĹ��캯�����
		
	}

}
