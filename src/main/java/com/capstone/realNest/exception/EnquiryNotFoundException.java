package com.capstone.realNest.exception;

public class EnquiryNotFoundException
        extends RuntimeException {

    public EnquiryNotFoundException(Long enquiryId) {
        super("Enquiry not found with id: " + enquiryId);
    }
}