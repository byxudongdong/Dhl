package com.gson;

import java.util.List;  
  
import com.google.gson.Gson;

public class SendJson{

   public static Task main(String ref_id,  
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
        if(doc_id == null)
        {
        	task1.setDocId("");
        }else {
        	task1.setDocId(doc_id);
		}
        if(task_id == null)
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
//        System.out.println("----------�򵥶���֮���ת��-------------");  
//        // �򵥵�beanתΪjson  
//        String TaskJson = gson.toJson(task1);  
//
//        System.out.println("��Beanת��ΪJson===" + TaskJson); 
        return task1;
        // jsonתΪ��Bean  
//      Task student = gson.fromJson(s1, Task.class);  
//      System.out.println("JsonתΪ��Bean===" + student);  
    }
	
	public static List<Task> SendTasks(List<Task> taskJsons, Task taskJson,Boolean endFlag) 
	{
		//Gson gson = new Gson();
		// TODO �Զ����ɵĹ��캯�����
		if(endFlag == false)
		{	
			
			//List<Task> taskJsons = new ArrayList<Task>();  
			taskJsons.add(taskJson);   
			
//			System.out.println("----------�����͵�List֮���ת��-------------");  
//			//�����͵�listת��Ϊjson  
//			String TaskList = gson.toJson(taskJsons);  
//			System.out.println("�����͵�listת��Ϊjson==" + TaskList);  
			
//			//jsonתΪ�����͵�list  
//			List<Task> retList = gson.fromJson(TaskList,  
//			        new TypeToken<List<Task>>() {  
//			        }.getType());  
//			for (Task tasks : retList) {  
//			    System.out.println(tasks);  
//			}
		}
		return taskJsons;
	}

}
