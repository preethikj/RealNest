package com.capstone.realNest.exception;

public class SelfEnquiryNotAllowedException
        extends RuntimeException {

    public SelfEnquiryNotAllowedException() {
        super("You cannot send an enquiry for your own property");
    }
}