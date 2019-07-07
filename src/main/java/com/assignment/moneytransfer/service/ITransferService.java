package com.assignment.moneytransfer.service;

import com.assignment.moneytransfer.api.TransferRequest;
import com.assignment.moneytransfer.api.TransferResponse;

public interface ITransferService {
    TransferResponse transferAmount(TransferRequest transferRequest);
}
