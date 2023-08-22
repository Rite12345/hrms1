package com.dizitiveit.hrms.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.hrms.dao.AttendanceDao;
import com.dizitiveit.hrms.dao.AttendanceSummaryDao;
import com.dizitiveit.hrms.dao.EmployeeDao;
import com.dizitiveit.hrms.dao.HolidaysDao;
import com.dizitiveit.hrms.model.Attendance;
import com.dizitiveit.hrms.model.AttendanceSummary;
import com.dizitiveit.hrms.model.Employee;
import com.dizitiveit.hrms.pojo.AttendPojo;
import com.dizitiveit.hrms.pojo.AttendancePojo;
import com.dizitiveit.hrms.pojo.Responses;

@RequestMapping("/attendance")
@RestController
public class AttendanceController {

	@Autowired
	private AttendanceDao attendanceDao;

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private HolidaysDao holidaysDao;
	
	@Autowired
	private AttendanceSummaryDao attendanceSummaryDao;
	
	

	@PostMapping("/addAttendanceDetails/{employeeId}")
	public ResponseEntity<?> addFamilyDetails(@RequestBody Attendance attendance, @PathVariable String employeeId) {
		Employee employee = employeeDao.findByEmployeeId(employeeId);
		 Attendance attendanceExisting = attendanceDao.findByattendanceDay(employee.getEmplyoeeCode(), attendance.getAttendanceDay());
         if(attendanceExisting==null)
         {
		attendance.setEmployee(employee);
		attendance.setCreatedAt(new Date());	
		ZoneId zone1 = ZoneId.of("Asia/Kolkata");
	    LocalTime time = LocalTime.now(zone1);
       System.out.println(time);
  
    String time1= attendance.getInTime().toString();
    String time2 =attendance.getOutTime().toString();

    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");  
    Date d1 = null;
    Date d2 = null;
    try {
        d1 = format.parse(time1);
        d2 = format.parse(time2);
    } catch (ParseException e) {
        e.printStackTrace();
    }    

    long diff = d2.getTime() - d1.getTime();
    
    long diffHours = diff / (60 * 60 * 1000);  
    String strHours=String.valueOf(diffHours);
   
    long diffMinutes =  (diff / (60 * 1000)) % 60;  
    String strMinutes=String.valueOf(diffMinutes);
    
    long diffSeconds = (diff / 1000) % 60 ;  
    String strSeconds=String.valueOf(diffSeconds);
   
    attendance.setTotalHours(strHours+ ":" + strMinutes+ ":" +strSeconds);
    attendanceDao.save(attendance);	
	return ResponseEntity.ok(Responses.builder().message("Attendance Details Saved Successfully.").build());
    }
         
    else 
         {
       	  return ResponseEntity.badRequest().body(Responses.builder().message("Attendance already exists with this date").build());
         }
         
	}

	
	@PostMapping("/updateAttendanceDetails/{attendanceId}/{month}/{year}")
	public ResponseEntity<?> updateAdditionalDetails(@RequestBody Attendance attendance,@PathVariable int attendanceId,@PathVariable long month, @PathVariable long year) {
		// Employee employee = employeeDao.findByEmployeeId(familyDetailsId);
		   
		Attendance attendUpdate = attendanceDao.findByattendanceId(attendanceId,month,year);
		if (attendUpdate != null) {

			attendUpdate.setAttendanceDay(attendance.getAttendanceDay());
			attendUpdate.setInTime(attendance.getInTime());
			attendUpdate.setOutTime(attendance.getOutTime());
			
	    	   ZoneId zone1 = ZoneId.of("Asia/Kolkata");
	    	    LocalTime time = LocalTime.now(zone1);
               System.out.println(time);
          
            String time1= attendUpdate.getInTime().toString();
            String time2 =attendUpdate.getOutTime().toString();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = format.parse(time1);
                d2 = format.parse(time2);
            } catch (ParseException e) {
                e.printStackTrace();
            }    

            long diff = d2.getTime() - d1.getTime();
            
            long diffHours = diff / (60 * 60 * 1000);  
            String strHours=String.valueOf(diffHours);
           
            long diffMinutes =  (diff / (60 * 1000)) % 60;  
            String strMinutes=String.valueOf(diffMinutes);
            
            long diffSeconds = (diff / 1000) % 60 ;  
            String strSeconds=String.valueOf(diffSeconds);
           
            attendUpdate.setTotalHours(strHours+ ":" + strMinutes+ ":" +strSeconds);
          	
			attendUpdate.setLastModifiedDate(new Date());
			attendanceDao.save(attendUpdate);
			return ResponseEntity.ok(Responses.builder().message("Attendance Details Updated Successfully.").build());
		} else {
			return ResponseEntity.badRequest().body(Responses.builder().message("Attendance Id not found").build());
		}
		
	}
	
	
	@GetMapping("getAttendanceDetails")
	public ResponseEntity<?> Attendance() {
		List<Attendance> attendDetails = attendanceDao.findAll();
		if (attendDetails.size() > 0) {
			HashMap<String, List<Attendance>> map = new HashMap<String, List<Attendance>>();
			map.put("Attendance", attendDetails);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.badRequest().body(Responses.builder().message("Data not found").build());
		}
	}

	@GetMapping("/getAttendance/{employeeId}")
	public ResponseEntity<?> getAttendance(@PathVariable String employeeId) {
		Employee attend = employeeDao.findByEmployeeId(employeeId);

		List<Attendance> attendDetails = attendanceDao.findByEmployee(attend);
		List<AttendancePojo> attenList = new ArrayList<>();

		for (Attendance attende : attendDetails) {
			AttendancePojo attendPojo1 = new AttendancePojo();
			attendPojo1.setEmployeeId(attende.getEmployee().getEmployeeId());
			attendPojo1.setAttendanceId(attende.getAttendanceId());
			 String empName=attende.getEmployee().getFirstName() + " " + attende.getEmployee().getLastName();
				attendPojo1.setEmpName(empName);
			
			DateFormat dfAttendance = new SimpleDateFormat("yyyy-MM-dd");
			if (attende.getAttendanceDay() != null) {
				attendPojo1.setAttendanceDay(dfAttendance.format(attende.getAttendanceDay()));
			}
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			if (attende.getInTime()!= null)
			 { 
			    attendPojo1.setInTime(formatter.format(attende.getInTime()));
			 }
			  
			//DateFormat dfOutTime = new SimpleDateFormat(("HH:mm:ss"));
			DateTimeFormatter formatt = DateTimeFormatter.ofPattern("HH:mm:ss");
			if (attende.getOutTime() != null)
			 {
			  attendPojo1.setOutTime(formatt.format(attende.getOutTime()));
			 }
				
			attendPojo1.setTotalHours(attende.getTotalHours());
			attenList.add(attendPojo1);
		}

		HashMap<String, List<AttendancePojo>> map = new HashMap<String, List<AttendancePojo>>();
		map.put("Attendance", attenList);
		return ResponseEntity.ok(map);
	}
	
	
	  @GetMapping("/getAttendDetails/{employeeId}")
	  public ResponseEntity<?> getAttendanceDetails(@PathVariable String employeeId) 
	  { 
		  
		  Employee emp = employeeDao.findByEmployeeId(employeeId);
	      List<Attendance> attendDetails=attendanceDao.findByEmployee(emp);
	      List<AttendancePojo> attenList = new ArrayList<>();
	      for(Attendance attende:attendDetails)
	      { 
	    	  AttendancePojo attendPojo1=new  AttendancePojo();
	         attendPojo1.setEmployeeId(attende.getEmployee().getEmployeeId());
	         attendPojo1.setAttendanceId(attende.getAttendanceId());
	         String empName=attende.getEmployee().getFirstName() + " " + attende.getEmployee().getLastName();
				attendPojo1.setEmpName(empName);
	         
	         DateFormat dfAttendance = new SimpleDateFormat("yyyy-MM-dd");
	   	     if(attende.getAttendanceDay()!=null)
	   	     {
	   	        attendPojo1.setAttendanceDay(dfAttendance.format(attende.getAttendanceDay()));
	   	      }	  
	        
	   	     //DateFormat dfIn = new SimpleDateFormat("HH:mm:ss");
	   		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		     if(attende.getInTime()!=null) 
		     {
		     attendPojo1.setInTime(formatter.format(attende.getInTime()));
		  
		      }
	 
	  
		     //DateFormat dfOutTime = new SimpleDateFormat(("HH:mm:ss"));
		      DateTimeFormatter formatt = DateTimeFormatter.ofPattern("HH:mm:ss");
			  if(attende.getOutTime()!=null)
			  {
			  attendPojo1.setOutTime(formatt.format(attende.getOutTime())); 
			  }
	  
			  attendPojo1.setTotalHours(attende.getTotalHours());
			  attenList.add(attendPojo1); 
			  }
			  
			  HashMap<String, List<AttendancePojo>> map=new HashMap<String,
			  List<AttendancePojo>>(); map.put("Attendance", attenList); 
			  return ResponseEntity.ok(map);
			 }
	
	 
	
		@GetMapping("/getAttend/{employeeId}/{month}/{year}")
		public ResponseEntity<?>  getAttend(@PathVariable String employeeId,@PathVariable long month,@PathVariable long year)
		{
		   Employee emp=employeeDao.findByEmployeeId(employeeId);
		   List<Attendance> attendDetails=attendanceDao.findByemployee(month,year,emp.getEmplyoeeCode());
		   List<AttendancePojo> attenList = new ArrayList<>();
		   for(Attendance attende:attendDetails)
		   {
			   AttendancePojo attendPojo=new AttendancePojo();
			   attendPojo.setEmployeeId(attende.getEmployee().getEmployeeId());
			   attendPojo.setAttendanceId(attende.getAttendanceId());
			   String empName=attende.getEmployee().getFirstName() + " " + attende.getEmployee().getLastName();
				attendPojo.setEmpName(empName);
			   DateFormat dfAttendance = new SimpleDateFormat("yyyy-MM-dd");
		   	   if(attende.getAttendanceDay()!=null)
		   	     {
		   	        attendPojo.setAttendanceDay(dfAttendance.format(attende.getAttendanceDay()));
		   	     }
		   	   
		   	  //DateFormat dfIn = new SimpleDateFormat("HH:mm:ss");
		   	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		      if(attende.getInTime()!=null) 
		       {
		         attendPojo.setInTime(formatter.format(attende.getInTime()));
		       }
	 
		      
		      //DateFormat dfOutTime = new SimpleDateFormat(("HH:mm:ss"));
		      DateTimeFormatter formatt = DateTimeFormatter.ofPattern("HH:mm:ss");
			  if(attende.getOutTime()!=null)
			  {
			  attendPojo.setOutTime(formatt.format(attende.getOutTime())); 
			  }
	  
			 
			   
			  attendPojo.setTotalHours(attende.getTotalHours());
			
			  attenList.add(attendPojo);
		   }
			
		   HashMap<String, List<AttendancePojo>> map=new HashMap<String,List<AttendancePojo>>(); 
		   map.put("Attendance", attenList); 
		   return ResponseEntity.ok(map);
	      
		}
	    

	    @DeleteMapping("/deleteAttendance/{attendanceId}")
		public ResponseEntity<?> deleteFamilyDetails(@PathVariable int attendanceId) {
			Attendance attendDetails = attendanceDao.findByattendanceId(attendanceId);
			if (attendDetails != null) 
			{
				attendanceDao.deleteById(attendanceId);
				return ResponseEntity.ok(Responses.builder().message("Attendance Details are Deleted Successfully.").build());
			} 
			else 
			{
				return ResponseEntity.badRequest().body(Responses.builder().message("Id not found").build());
			}
		}
	    
	    
	    @PostMapping("/webClockIn/{employeeId}")
		public ResponseEntity<?> webClockIn(@PathVariable String employeeId)
		{
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			Attendance attendance = new Attendance();
		     attendance.setAttendanceDay(new Date());
		     LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		     long day = localDate.getDayOfMonth();
		     long month = localDate.getMonthValue();
				long year = localDate.getYear();
		       
		       Attendance attendanceExisting = attendanceDao.getByAttendanceDay(employee.getEmplyoeeCode(),day,month,year);
		       if(attendanceExisting==null)
		       {
		    	   ZoneId zone1 = ZoneId.of("Asia/Kolkata");
		    	    LocalTime time = LocalTime.now(zone1);
	               System.out.println(time);
		    	   attendance.setInTime(time);
		    	   attendance.setEmployee(employee);
		    	   attendanceDao.save(attendance);
		    	   return ResponseEntity.ok(Responses.builder().message("You are logged in sucessfully").build());
		       }
		       else {
		    	   return ResponseEntity.badRequest().body(Responses.builder().message("You have already logged in").build());
		       }
		       
		       
		}
		
		
		public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
			  long diffInMillies = date1.getTime() - date2.getTime();
			  
			  return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS); 
			  }
		
		@PostMapping("webClockOut/{employeeId}")
		public ResponseEntity<?> webClockOut(@PathVariable String employeeId)
		{
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		     long day = localDate.getDayOfMonth();
		     long month = localDate.getMonthValue();
				long year = localDate.getYear();
		      
		       Attendance attendanceExisting = attendanceDao.getByAttendanceDay(employee.getEmplyoeeCode(),day,month,year);
		       if(attendanceExisting!=null)
		       {
		    	   ZoneId zone1 = ZoneId.of("Asia/Kolkata");
		    	    LocalTime time = LocalTime.now(zone1);
	               System.out.println(time);
	               attendanceExisting.setOutTime(time);
	               String time1= attendanceExisting.getInTime().toString();
	               String time2 =attendanceExisting.getOutTime().toString();
	               SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	               Date d1 = null;
	               Date d2 = null;
	               try {
	                   d1 = format.parse(time1);
	                   d2 = format.parse(time2);
	               } catch (ParseException e) {
	                   e.printStackTrace();
	               }
	               long diff = d2.getTime() - d1.getTime();
	              
	               long diffHours = diff / (60 * 60 * 1000);
	               String strHours=String.valueOf(diffHours);
	             
	               long diffMinutes =  (diff / (60 * 1000)) % 60;
	               String strMinutes=String.valueOf(diffMinutes);
	              
	               long diffSeconds = (diff / 1000) % 60 ;
	               String strSeconds=String.valueOf(diffSeconds);
	             
	               attendanceExisting.setTotalHours(strHours+ ":" + strMinutes+ ":" +strSeconds);
	             
	               attendanceDao.save(attendanceExisting);
	               AttendanceSummary attendanceSummary = new AttendanceSummary();
	               attendanceSummary.setOutTime(time);
	               attendanceSummary.setAttendanceDay(new Date());
	               attendanceSummary.setEmployee(employee);
	               attendanceSummaryDao.save(attendanceSummary);
	               return ResponseEntity.ok(Responses.builder().message("You are logged out sucessfully").build());
		       }
		       else {
		    	   return ResponseEntity.badRequest().body(Responses.builder().message("You have not logged in").build());
		       }
		}
		
		
		@GetMapping("getTodayAttendance/{employeeId}")
		public ResponseEntity<?> getTodayAttendance(@PathVariable String employeeId) {
			Employee employee = employeeDao.findByEmployeeId(employeeId);
			LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			long day = localDate.getDayOfMonth();
			long month = localDate.getMonthValue();
			long year = localDate.getYear();
			Attendance attendance = attendanceDao.getAttendanceToday(employee.getEmplyoeeCode(), day, month, year);
			if (attendance != null) {
				AttendancePojo attendancePojo = new AttendancePojo();

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if (attendance.getAttendanceDay() != null) {
					attendancePojo.setAttendanceDay(df.format(attendance.getAttendanceDay()));
				}

				String empName = attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName();
				attendancePojo.setEmpName(empName);

				attendancePojo.setEmployeeId(attendance.getEmployee().getEmployeeId());
				attendancePojo.setAttendanceId(attendance.getAttendanceId());

				DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("HH:mm:ss");
				if (attendance.getInTime() != null) {
					attendancePojo.setInTime(formatterIn.format(attendance.getInTime()));
				}

				DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("HH:mm:ss");
				if (attendance.getOutTime() != null) {
					attendancePojo.setOutTime(formatterOut.format(attendance.getOutTime()));
				}
				attendancePojo.setTotalHours(attendance.getTotalHours());

				HashMap<String, AttendancePojo> map = new HashMap<String, AttendancePojo>();
				map.put("EmployeeAttendance", attendancePojo);
				return ResponseEntity.ok(map);
			} else {
				return ResponseEntity.badRequest().body(Responses.builder().message("Employee Haven't LoggedIn").build());
			}

		}
		
		
		@PostMapping("countWeekendDays/{year}/{month}")
		public int countMonthWeekDays(@PathVariable int year,@PathVariable int month) {
		    Calendar calendar = Calendar.getInstance();
		    // Note that month is 0-based in calendar, bizarrely.
		    calendar.set(year, month - 1, 1);
		    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		    int count = 0;
		    for (int day = 1; day <= daysInMonth; day++) {
		        calendar.set(year, month - 1, day);
		        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
		            count++;
		            System.out.println("count"+count);
		            // Or do whatever you need to with the result.
		        }
		    }
		    return count;
		  //if(dayOfWeek==Calendar.SATURDAY)
        	//{
        		//saturDayCount++;
        		//if(saturDayCount==2||saturDayCount==4)
        		//{
        			//count++;
        			
        		//}
        	  // count++;
        	//}
        		//else
        		//{
		        	//count++;
		        	
		        //}
		}
		
		  @PostMapping("/CurrentMonth/{month}/{year}/{employeeId}")
			public int workingDaysCount(@PathVariable int month, @PathVariable int year,@PathVariable String employeeId)
			
			{
			  Employee emp = employeeDao.findByEmployeeId(employeeId);
			List<Date> holidaysList=holidaysDao.getbyHolidaysMonth(month, year);
			int holidays=holidaysList.size();
			System.out.println("HolidaysSize: " +holidays);
			
			List<Attendance> attendanceList=attendanceDao.getByLop(month,year,emp.getEmplyoeeCode());
			int attendance=attendanceList.size();
			System.out.println("AttendanceSize: " +attendance);
			//year = 0;
		    Calendar calendar = Calendar.getInstance();
		   calendar.set(year, month-1, 1);
		    int daysInMonth = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
		    System.out.println("DaysInMonth: " +daysInMonth);
		    int count = countMonthWeekDays(year,month);	       
		             System.out.println("holidays list"+holidaysList.size());
		             int workingDays=daysInMonth-count-holidaysList.size();
		             System.out.println("workingDays: " + workingDays);
		             System.out.println("weekend count"+count);
		             return workingDays;
			}
		
		  
		  @GetMapping("/getAttendanceView/{employeeId}/{month}/{year}")
			public ResponseEntity<?> getAttendance(@PathVariable String employeeId, @PathVariable int month,
					@PathVariable int year) {
				Employee emp = employeeDao.findByEmployeeId(employeeId);
				if(emp!=null)
				{
				List<Attendance> attendDetails = attendanceDao.findByemployee(month, year, emp.getEmplyoeeCode());
				AttendPojo attendPojo = new AttendPojo();
				for (Attendance attende : attendDetails) {
					
					attendPojo.setEmployeeId(attende.getEmployee().getEmployeeId());
					
					String empName = attende.getEmployee().getFirstName() + " " + attende.getEmployee().getLastName();
					attendPojo.setEmpName(empName);
					int lop=workingDaysCount(month, year,emp.getEmployeeId());
					attendPojo.setAbsentDays(lop);		
				}
				HashMap<String, AttendPojo> map = new HashMap<String, AttendPojo>();
				map.put("Attendance", attendPojo);
				return ResponseEntity.ok(map);
				
				}
				else
				{
					return ResponseEntity.badRequest().body(Responses.builder().message("No Data for Employee").build());
				}
			

			}

			
}
