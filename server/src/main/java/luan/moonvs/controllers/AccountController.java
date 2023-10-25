package luan.moonvs.controllers;

import luan.moonvs.models.requests.AccountRequestDTO;
import luan.moonvs.models.requests.AuthRequestDTO;
import luan.moonvs.models.requests.DeleteRequestDto;
import luan.moonvs.models.requests.PasswordRequestDTO;
import luan.moonvs.models.responses.AccountResponseDTO;
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
    public ResponseEntity<AccountResponseDTO> account() {
        return accountService.account();
    }

    @PatchMapping("/update")
    public ResponseEntity<AccountResponseDTO> updateAccount(@RequestBody AccountRequestDTO accountDto) {
        return accountService.updateAccount(accountDto);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordRequestDTO passwordRequestDTO) {
        return accountService.updatePassword(passwordRequestDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestBody DeleteRequestDto deleteRequestDto) {
        return accountService.deleteAccount(deleteRequestDto);
    }
}
