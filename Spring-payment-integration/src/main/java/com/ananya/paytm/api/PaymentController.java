package com.ananya.paytm.api;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.paytm.pg.merchant.CheckSumServiceHelper;

@Controller
public class PaymentController {
	
	@Autowired
	private PaytmDetails paytmDetails;
	@Autowired
	private Environment env;
	
	@GetMapping("/")
	public String donate() {
		return "donate";
	}
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	 @Autowired
	public JavaMailSender javamails;

    
		SimpleMailMessage m= new SimpleMailMessage();
		Random rand = new Random(); 
		int value = rand.nextInt(100000050); 
	
	 @PostMapping(value = "/pgredirect")
	    public ModelAndView getRedirect(@RequestParam(name = "Email_Id") String email,
	    		@RequestParam(name = "Mobile") String  mobile_no,
	                                    @RequestParam(name = "TXN_AMOUNT")
	    String transactionAmount)
	                                    throws Exception {

 				m.setTo(email);
	        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmDetails.getPaytmUrl());
	        TreeMap<String, String> parameters = new TreeMap<>();
	        paytmDetails.getDetails().forEach((k, v) -> parameters.put(k, v));
	        parameters.put("MOBILE_NO", mobile_no);
	     
	        parameters.put("ORDER_ID", String.valueOf(++value));
	   
	        parameters.put("TXN_AMOUNT", transactionAmount);
	        parameters.put("CUST_ID", email);
	        String checkSum = getCheckSum(parameters);
	        parameters.put("CHECKSUMHASH", checkSum);
	        modelAndView.addAllObjects(parameters);
	        return modelAndView;
	     
	    }

	 
	 @PostMapping(value = "/pgresponse")
	    public String getResponseRedirect(HttpServletRequest request, Model model) {
		 	
	        Map<String, String[]> mapData = request.getParameterMap();
	        TreeMap<String, String> parameters = new TreeMap<String, String>();
	        mapData.forEach((key, val) -> parameters.put(key, val[0]));
	        String paytmChecksum = "";
	        if (mapData.containsKey("CHECKSUMHASH")) {
	            paytmChecksum = mapData.get("CHECKSUMHASH")[0];
	        }
	        String result;

	        boolean isValideChecksum = false;
	        System.out.println("RESULT : "+parameters.toString());
	        try {
	            isValideChecksum = validateCheckSum(parameters, paytmChecksum);
	            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
	                if (parameters.get("RESPCODE").equals("01")) {
	                    result = "Payment Successful";
	                   
	                		m.setSubject(result);
	                		m.setText("You have Successfully donated amount of Rs. "+parameters.get("TXNAMOUNT")+" on "+parameters.get("TXNDATE")+"\n Thank You So Much.");
	                		javamails.send(m);
	                    
	                    
	                    
	                } else {
	                    result = "Payment Failed";
	                }
	            } else {
	                result = "Checksum mismatched";
	            }
	        } catch (Exception e) {
	            result = e.toString();
	        
	        }
	        
	        model.addAttribute("result",result);
	        parameters.remove("CHECKSUMHASH");
	        model.addAttribute("parameters",parameters);
	        return "report";
	    }

	    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
	        return CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(paytmDetails.getMerchantKey(),
	                parameters, paytmChecksum);
	    }


	private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
		return CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(paytmDetails.getMerchantKey(), parameters);
	}
	
	
	
}
