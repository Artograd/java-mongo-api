package me.artograd.javamongoapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import me.artograd.javamongoapi.model.Tender;
import me.artograd.javamongoapi.model.system.UserTokenClaims;
import me.artograd.javamongoapi.services.CognitoService;
import me.artograd.javamongoapi.services.TenderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tenders")
public class TenderController {
	
    @Autowired
    private TenderService tenderService;
    
    @Autowired
    private CognitoService cognitoService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Tender> createTender(@RequestBody Tender tender, HttpServletRequest request) {
    	
    	UserTokenClaims claims = cognitoService.getUserTokenClaims(request);
    	
    	//operation is allowed only for officer's token
    	if ( claims.getUsername() == null || !claims.getUsername().equals( tender.getOwnerId() ) || !claims.isOfficer() ) {
    		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    	}
    	
    	Tender createdTender = tenderService.createTender(tender);
        return new ResponseEntity<>(createdTender, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tender> getTender(@PathVariable String id) {
        Tender tender = tenderService.getTender(id);
        if (tender != null) {
            return new ResponseEntity<>(tender, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Tender> updateTender(@PathVariable String id, @RequestBody Tender tender, HttpServletRequest request) {
    	
    	if ( isDenied(id, request) ) { return new ResponseEntity<>(HttpStatus.FORBIDDEN);}
    	
        Tender updatedTender = tenderService.updateTender(tender);
        if (updatedTender != null) {
            return new ResponseEntity<>(updatedTender, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteTender(@PathVariable String id, HttpServletRequest request) {
    	
    	if ( isDenied(id, request) ) { return new ResponseEntity<>(HttpStatus.FORBIDDEN);}
    	
        tenderService.deleteTender(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Tender>> searchTenders(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<String> locationLeafIds,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(required = false) String ownerId) {
        List<Tender> tenders = tenderService.searchTenders(title, 
        		locationLeafIds != null ? locationLeafIds : new ArrayList<>(), 
        		statuses  != null ? statuses  : new ArrayList<>(),
        				ownerId);
        return new ResponseEntity<>(tenders, HttpStatus.OK);
    }
    
    /**
     * Check that operation is executed by tender's owner and owner is still official
     * @param tenderId
     * @param request
     * @return
     */
    private boolean isDenied(String tenderId, HttpServletRequest request) {
    	Tender existingTender = tenderService.getTender(tenderId);
    	UserTokenClaims claims = cognitoService.getUserTokenClaims(request);
    	if ( claims.getUsername() == null || //user name is not provided in token
    		!claims.getUsername().equals( existingTender.getOwnerId() ) || //request is made on behalf of different user
    		!claims.isOfficer() //user is no more an officer
    			) {
    		return true;
    	}
    	return false;
    }
    
}
