package com.ananya.paytm.api;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;




@Component
@ConfigurationProperties("paytm.payment.sandbox")

public class PaytmDetails {
	
	private String merchantId;

    private String merchantKey;


    private String website;


    private String paytmUrl;

    private Map<String, String> details;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}



	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}


	public String getPaytmUrl() {
		return paytmUrl;
	}

	public void setPaytmUrl(String paytmUrl) {
		this.paytmUrl = paytmUrl;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}
	public PaytmDetails() {
		
	}
	public PaytmDetails(String merchantId, String merchantKey, String website, String paytmUrl,
			Map<String, String> details) {
		super();
		this.merchantId = merchantId;
		this.merchantKey = merchantKey;
		this.website = website;
		this.paytmUrl = paytmUrl;
		this.details = details;
	}

	@Override
	public String toString() {
		return "PaytmDetails [merchantId=" + merchantId + ", merchantKey=" + merchantKey + ", website=" + website
				+ ", paytmUrl=" + paytmUrl + ", details=" + details + "]";
	}


}
