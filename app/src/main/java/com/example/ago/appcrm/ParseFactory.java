package com.example.ago.appcrm;

import org.json.JSONObject;

/**
 * Created by Wei on 2016/6/27.
 */
public class ParseFactory {

    public static Product parseProduct(JSONObject productObject) {
        Product product = new Product();
        try {
            product.setClassification(productObject.getString("classification"));
            product.setIntroduction(productObject.getString("introduction"));
            product.setProductid(productObject.getString("productid"));
            product.setProductname(productObject.getString("productname"));
            product.setProductremarks(productObject.getString("productremarks"));
            product.setProductsn(productObject.getString("productsn"));
            product.setSalesunit(productObject.getString("salesunit"));
            product.setStandardprice(productObject.getString("standardprice"));
            product.setUnitcost(productObject.getString("unitcost"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    public static Followup parseFollowup(JSONObject followupObject) {
        Followup followup = new Followup();
        try {
            followup.setContent(followupObject.getString("content"));
            followup.setCreatetime(followupObject.getString("createtime"));
            followup.setCreatorid(followupObject.getString("creatorid"));
            followup.setFollowupid(followupObject.getString("followupid"));
            followup.setFollowupremarks(followupObject.getString("followupremarks"));
            followup.setFollowuptype(followupObject.getString("followuptype"));
            followup.setName(followupObject.getString("name"));
            followup.setSourceid(followupObject.getString("sourceid"));
            followup.setSourcetype(followupObject.getString("sourcetype"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return followup;
    }

    public static Contract parseContract(JSONObject contractObject) {
        Contract contract = new Contract();
        try {
            contract.setSuccessrate(contractObject.getString("successrate"));
            contract.setZipcode(contractObject.getString("zipcode"));
            contract.setWebsite(contractObject.getString("website"));
            contract.setStaffid(contractObject.getString("staffid"));
            contract.setTelephone(contractObject.getString("telephone"));
            contract.setAcquisitiondate(contractObject.getString("acquisitiondate"));
            contract.setAddress(contractObject.getString("address"));
            contract.setAttachment(contractObject.getString("attachment"));
            contract.setBusinesstype(contractObject.getString("businesstype"));
            contract.setChannel(contractObject.getString("channel"));
            contract.setClientcontractor(contractObject.getString("clientcontractor"));
            contract.setContractid(contractObject.getString("contractid"));
            contract.setContractnumber(contractObject.getString("contractnumber"));
            contract.setContractremarks(contractObject.getString("contractremarks"));
            contract.setContractstatus(contractObject.getString("contractstatus"));
            contract.setContracttitle(contractObject.getString("contracttitle"));
            contract.setContracttype(contractTypeParse(contractObject.getString("contracttype")));
            contract.setCreatedate(contractObject.getString("createdate"));
            contract.setCustomerid(contractObject.getString("customerid"));
            contract.setCustomername(contractObject.getString("customername"));
            contract.setCustomerremarks(contractObject.getString("customerremarks"));
            contract.setCustomersource(contractObject.getString("customersource"));
            contract.setCustomerstatus(contractObject.getString("customerstatus"));
            contract.setCustomertype(contractObject.getString("customertype"));
            contract.setEmail(contractObject.getString("email"));
            contract.setEnddate(contractObject.getString("enddate"));
            contract.setEstimatedamount(contractObject.getString("estimatedamount"));
            contract.setExpecteddate(contractObject.getString("expecteddate"));
            contract.setOpportunitiessource(contractObject.getString("opportunitiessource"));
            contract.setOpportunityid(contractObject.getString("opportunityid"));
            contract.setOpportunityremarks(contractObject.getString("opportunityremarks"));
            contract.setOpportunitystatus(contractObject.getString("opportunitystatus"));
            contract.setOpportunitytitle(contractObject.getString("opportunitytitle"));
            contract.setOurcontractor(contractObject.getString("ourcontractor"));
            contract.setParentcustomerid(contractObject.getString("parentcustomerid"));
            contract.setPaymethod(contractObject.getString("paymethod"));
            contract.setProfile(contractObject.getString("profile"));
            contract.setRegionid(contractObject.getString("regionid"));
            contract.setSigningdate(contractObject.getString("signingdate"));
            contract.setSize(contractObject.getString("size"));
            contract.setStaffid(contractObject.getString("staffid"));
            contract.setStartdate(contractObject.getString("startdate"));
            contract.setTotalamount(contractObject.getString("totalamount"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contract;
    }

    public static Opportunity parseOpportunity(JSONObject opportunityObject) {
        Opportunity opportunity = new Opportunity();
        try {
            opportunity.setZipcode(opportunityObject.getString("zipcode"));
            opportunity.setWebsite(opportunityObject.getString("website"));
            opportunity.setTelephone(opportunityObject.getString("telephone"));
            opportunity.setAcquisitiondate(opportunityObject.getString("acquisitiondate"));
            opportunity.setAddress(opportunityObject.getString("address"));
            opportunity.setBusinesstype(businessTypeParse(opportunityObject.getString("businesstype")));
            opportunity.setChannel(opportunityObject.getString("channel"));
            opportunity.setCreatedate(opportunityObject.getString("createdate"));
            opportunity.setCustomerid(opportunityObject.getString("customerid"));
            opportunity.setCustomername(opportunityObject.getString("customername"));
            opportunity.setCustomerremarks(opportunityObject.getString("customerremarks"));
            opportunity.setCustomersource(opportunityObject.getString("customersource"));
            opportunity.setCustomerstatus(opportunityObject.getString("customerstatus"));
            opportunity.setCustomertype(opportunityObject.getString("customertype"));
            opportunity.setEmail(opportunityObject.getString("email"));
            opportunity.setEstimatedamount(opportunityObject.getString("estimatedamount"));
            opportunity.setExpecteddate(opportunityObject.getString("expecteddate"));
            opportunity.setOpportunitiessource(opportunityObject.getString("opportunitiessource"));
            opportunity.setOpportunityid(opportunityObject.getString("opportunityid"));
            opportunity.setOpportunityremarks(opportunityObject.getString("opportunityremarks"));
            opportunity.setOpportunitystatus(opportunityObject.getString("opportunitystatus"));
            opportunity.setOpportunitytitle(opportunityObject.getString("opportunitytitle"));
            opportunity.setParentcustomerid(opportunityObject.getString("parentcustomerid"));
            opportunity.setProfile(opportunityObject.getString("profile"));
            opportunity.setRegionid(opportunityObject.getString("regionid"));
            opportunity.setSize(opportunityObject.getString("size"));
            opportunity.setStaffid(opportunityObject.getString("staffid"));
            opportunity.setSuccessrate(opportunityObject.getString("successrate"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opportunity;
    }

    public static Contact parseContact(JSONObject contactObject) {
        Contact contact = new Contact();
        try {
            contact.setAddress(contactObject.getString("address"));
            contact.setContactsaddress(contactObject.getString("contactsaddress"));
            contact.setContactsage(contactObject.getString("contactsage"));
            contact.setContactsdeptname(contactObject.getString("contactsdeptname"));
            contact.setContactsemail(contactObject.getString("contactsemail"));
            contact.setContactsgender(contactObject.getString("contactsgender"));
            contact.setContactsid(contactObject.getString("contactsid"));
            contact.setContactsmobile(contactObject.getString("contactsmobile"));
            contact.setContactsname(contactObject.getString("contactsname"));
            contact.setContactsaddress(contactObject.getString("contactsaddress"));
            contact.setContactsposition(contactObject.getString("contactsposition"));
            contact.setContactsqq(contactObject.getString("contactsqq"));
            contact.setContactsremarks(contactObject.getString("contactsremarks"));
            contact.setContactstelephone(contactObject.getString("contactstelephone"));
            contact.setContactswangwang(contactObject.getString("contactswangwang"));
            contact.setContactswechat(contactObject.getString("contactswechat"));
            contact.setContactszipcode(contactObject.getString("contactszipcode"));
            contact.setCreatedate(contactObject.getString("createdate"));
            contact.setCustomerid(contactObject.getString("customerid"));
            contact.setCustomername(contactObject.getString("customername"));
            contact.setCustomerremarks(contactObject.getString("customerremarks"));
            contact.setCustomersource(contactObject.getString("customersource"));
            contact.setCustomerstatus(contactObject.getString("customerstatus"));
            contact.setCustomertype(contactObject.getString("customertype"));
            contact.setEmail(contactObject.getString("email"));
            contact.setParentcustomerid(contactObject.getString("parentcustomerid"));
            contact.setProfile(contactObject.getString("profile"));
            contact.setRegionid(contactObject.getString("regionid"));
            contact.setSize(contactObject.getString("size"));
            contact.setStaffid(contactObject.getString("staffid"));
            contact.setTelephone(contactObject.getString("telephone"));
            contact.setWebsite(contactObject.getString("website"));
            contact.setZipcode(contactObject.getString("zipcode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    public static Customer parseCustomer(JSONObject customerObject) {
        Customer customer = new Customer();
        try {
            customer.setAddress(customerObject.getString("address"));
            customer.setAvatar(customerObject.getString("avatar"));
            customer.setCreatedate(customerObject.getString("createdate"));
            customer.setCustomerid(customerObject.getString("customerid"));
            customer.setCustomername(customerObject.getString("customername"));
            customer.setCustomerremarks(customerObject.getString("customerremarks"));
            customer.setCustomersource(customerObject.getString("customersource"));
            customer.setCustomerstatus(customerObject.getString("customerstatus"));
            customer.setCustomertype(customerTypeParse(customerObject.getString("customertype")));
            customer.setDepartmentid(customerObject.getString("departmentid"));
            customer.setEmail(customerObject.getString("email"));
            customer.setEnable(customerObject.getString("enable"));
            customer.setExtattr(customerObject.getString("extattr"));
            customer.setGender(customerObject.getString("gender"));
            customer.setLeaderflag(customerObject.getString("leaderflag"));
            customer.setMobile(customerObject.getString("mobile"));
            customer.setName(customerObject.getString("name"));
            customer.setOpenid(customerObject.getString("openid"));
            customer.setOrder(customerObject.getString("order"));
            customer.setParentcustomerid(customerObject.getString("parentcustomerid"));
            customer.setPosition(customerObject.getString("position"));
            customer.setProfile(customerObject.getString("profile"));
            customer.setRegionid(customerObject.getString("regionid"));
            customer.setSize(customerObject.getString("size"));
            customer.setStaffid(customerObject.getString("staffid"));
            customer.setStaffremarks(customerObject.getString("staffremarks"));
            customer.setStaffstatus(customerObject.getString("staffstatus"));
            customer.setTel(customerObject.getString("tel"));
            customer.setTelephone(customerObject.getString("telephone"));
            customer.setUserid(customerObject.getString("userid"));
            customer.setWebsite(customerObject.getString("website"));
            customer.setWeixinid(customerObject.getString("weixinid"));
            customer.setZipcode(customerObject.getString("zipcode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public static String customerTypeParse(String opt) {
        String result = "重要客户";
        switch (opt){
            case "1":
                result = "重要客户";
                break;
            case "2":
                result = "一般客户";
                break;
            case "3":
                result = "低价值客户";
                break;
            default:
                break;
        }
        return result;
    }

    private static String contractTypeParse(String opt) {
        String result = "重要合同";
        switch (opt){
            case "1":
                result = "重要合同";
                break;
            case "2":
                result = "一般合同";
                break;
            case "3":
                result = "低价值合同";
                break;
            default:
                break;
        }
        return result;
    }

    private static String businessTypeParse(String opt) {
        String result = "一般商机";
        switch (opt) {
            case "2":
                result = "一般商机";
                break;
            case "1":
                result = "重要商机";
                break;
            default:
                break;
        }
        return result;
    }

    public static String customerTypeReverse(String opt) {
        String result = "1";
        switch (opt){
            case "重要客户":
                result = "1";
                break;
            case "一般客户":
                result = "2";
                break;
            case "低价值客户":
                result = "3";
                break;
            default:
                break;
        }
        return result;
    }

}
