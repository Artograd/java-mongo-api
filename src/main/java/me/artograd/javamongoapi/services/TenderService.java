package me.artograd.javamongoapi.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.artograd.javamongoapi.model.Tender;
import me.artograd.javamongoapi.model.User;
import me.artograd.javamongoapi.model.UserAttribute;
import me.artograd.javamongoapi.repositories.TenderRepository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class TenderService {

	@Autowired
    private TenderRepository tenderRepository;
	
	@Autowired
    private CognitoService cognitoService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Tender createTender(Tender tender) {
		tender.setModifiedAt( new Date() );
		tender.setCreatedAt( tender.getModifiedAt() );
		tender = setOwnerData(tender);
        return tenderRepository.save(tender);
    }

    public Tender getTender(String id) {
        return tenderRepository.findById(id).orElse(null);
    }

    public Tender updateTender(Tender tender) {
    	tender.setModifiedAt( new Date() );
    	tender = setOwnerData(tender);
    	return tenderRepository.save(tender);
    }

    public void deleteTender(String id) {
        tenderRepository.deleteById(id);
    }

    public List<Tender> searchTenders(String title, List<String> locations, List<String> statuses, String owner) {
    	final Query query = new Query();
        final List<Criteria> criteria = new ArrayList<>();

        if ( title != null && !title.isBlank() ) {
        	criteria.add(Criteria.where("title").regex(title, "i"));
        }
        
        if ( owner != null && !owner.isBlank() ) {
        	criteria.add(Criteria.where("owner").is(owner));
        }
        
        if (locations != null && !locations.isEmpty()) {
            criteria.add(Criteria.where("location").in(locations));
        }

        if (statuses != null && !statuses.isEmpty()) {
            criteria.add(Criteria.where("status").in(statuses));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, Tender.class);
    }
    
    private Tender setOwnerData(Tender tender) {
    	String sub = tender.getOwnerId();
    	if (StringUtils.isNotBlank(sub)) {
    		User user = cognitoService.getUserBySub(sub);
    		if (user != null ) {
    			List<UserAttribute> attributes = user.getAttributes();
    			if ( attributes != null ) {
    				String name = "";
    				for (UserAttribute userAttribute : attributes) {
        				if ( userAttribute.getName().equals("picture") ) {
        					tender.setOwnerPicture( userAttribute.getValue() );
        				}
        				if ( userAttribute.getName().equals("given_name") ) {
        					name = userAttribute.getValue() + name;
        				}
        				if ( userAttribute.getName().equals("family_name") ) {
        					name = name + " " + userAttribute.getValue();
        				}
        				if ( userAttribute.getName().equals("custom:organization") ) {
        					tender.setOrganization( userAttribute.getValue() );
        				}
        			}
    				
    				tender.setOwnerName( name.trim() );
    			}
    		}
    		
    	}
    	return tender;
    }
}
