package luan.moonvs.controllers;

import luan.moonvs.models.requests.AccountRequest;
import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.requests.PasswordRequest;
import luan.moonvs.models.responses.AccountResponse;
import luan.moonvs.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<AccountResponse> account() {
        return accountService.account();
    }

    @PatchMapping("/update")
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody AccountRequest accountDto) {
        return accountService.updateAccount(accountDto);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordRequest passwordRequest) {
        return accountService.updatePassword(passwordRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestBody AuthRequest authRequest) {
        return accountService.deleteAccount(authRequest);
    }
}
