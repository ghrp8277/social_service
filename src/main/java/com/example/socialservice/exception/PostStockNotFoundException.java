package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class PostStockNotFoundException extends GrpcException {
    public PostStockNotFoundException() {
        super(ErrorCodes.POST_STOCK_NOT_FOUND_CODE, ErrorMessages.POST_STOCK_NOT_FOUND);
    }
}
