package com.digibnk.account.grpc;

import com.digibnk.account.dto.BalanceUpdateRequest;
import com.digibnk.account.service.AccountService;
import com.digibnk.common.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class AccountGrpcService extends AccountServiceGrpc.AccountServiceImplBase {

    private final AccountService accountService;

    @Override
    public void debit(DebitRequest request, StreamObserver<DebitResponse> responseObserver) {
        log.info("gRPC Debit request for account: {}", request.getAccountId());
        try {
            BigDecimal amount = new BigDecimal(request.getAmount());
            var account = accountService.updateBalance(request.getAccountId(), new BalanceUpdateRequest(amount.negate()));
            
            responseObserver.onNext(DebitResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Debit successful")
                    .setNewBalance(account.getBalance().toString())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC Debit failed", e);
            responseObserver.onNext(DebitResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void credit(CreditRequest request, StreamObserver<CreditResponse> responseObserver) {
        log.info("gRPC Credit request for account: {}", request.getAccountId());
        try {
            BigDecimal amount = new BigDecimal(request.getAmount());
            var account = accountService.updateBalance(request.getAccountId(), new BalanceUpdateRequest(amount));
            
            responseObserver.onNext(CreditResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Credit successful")
                    .setNewBalance(account.getBalance().toString())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC Credit failed", e);
            responseObserver.onNext(CreditResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAccountDetails(AccountDetailsRequest request, StreamObserver<AccountDetailsResponse> responseObserver) {
        log.info("gRPC GetAccountDetails for account: {}", request.getAccountId());
        try {
            var account = accountService.getAccountById(request.getAccountId());
            
            responseObserver.onNext(AccountDetailsResponse.newBuilder()
                    .setId(account.getId())
                    .setAccountNumber(account.getAccountNumber())
                    .setStatus(account.getStatus().name())
                    .setBalance(account.getBalance().toString())
                    .setCurrency(account.getCurrency())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC GetAccountDetails failed", e);
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }
}
