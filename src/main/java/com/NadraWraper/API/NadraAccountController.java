package com.NadraWraper.API;

import com.NadraWraper.Service.NadraService;
import com.NadraWraper.Model.entity.NadraSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/nadra")
public class NadraAccountController {

    @Autowired
    private NadraService nadraService;


    @PostMapping("/addAccount")
    public ResponseEntity<String> addUser(@RequestBody NadraSchema nadraschema) {

        System.out.println("=================================================");
        System.out.println("Received request to add user: " + nadraschema);
        System.out.println("=================================================");

        // Validate the inputs
        if (nadraschema.getAccountType() == null || nadraschema.getAccountType().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Account type is required", HttpStatus.BAD_REQUEST);
        }
        if (nadraschema.getCnic() == null || nadraschema.getCnic().trim().isEmpty()) {
            return new ResponseEntity<>("Error: CNIC is required", HttpStatus.BAD_REQUEST);
        }
        if (nadraschema.getCnicIssueDate() == null) {
            return new ResponseEntity<>("Error: CNIC issue date is required", HttpStatus.BAD_REQUEST);
        }
        if (nadraschema.getMobile() == null || nadraschema.getMobile().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Mobile number is required", HttpStatus.BAD_REQUEST);
        }
        if (nadraschema.getAccountType() == null || nadraschema.getAccountType().trim().isEmpty()) {
            return new ResponseEntity<>("Error: AccountType is required", HttpStatus.BAD_REQUEST);
        }
        if (nadraschema.getRecordType() == null || nadraschema.getRecordType().trim().isEmpty()) {
            return new ResponseEntity<>("Error: RecordType is required", HttpStatus.BAD_REQUEST);
        }

        // Convert LocalDate to String (assuming format is yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cnicIssueDateStr = nadraschema.getCnicIssueDate().format(formatter); // Convert LocalDate to String

        return nadraService.saveAccount(nadraschema.getEmail(), nadraschema.getAccountType(), nadraschema.getCnic(), cnicIssueDateStr, nadraschema.getMobile(), nadraschema.getRecordType(), nadraschema.getNtn(), nadraschema.getPin(), nadraschema.getSubRecords());
    }




    @GetMapping("/getAccountsDetails")
    public ResponseEntity<?> getPrimaryAccountsWithSubs() {
        try {
            List<NadraSchema> data = nadraService.findAllUsers();

            if (data == null ) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/updateAccount")
    public ResponseEntity<?> updateUser(@RequestBody NadraSchema nadraschema) {

        System.out.println("=================================================");
        System.out.println("Received request to update user: " + nadraschema);
        System.out.println("=================================================");

        // Convert LocalDate to String (assuming format is yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cnicIssueDateStr = nadraschema.getCnicIssueDate().format(formatter); // Convert LocalDate to String

        return nadraService.updateAccount(nadraschema.getId(), nadraschema.getEmail(), nadraschema.getAccountType(), nadraschema.getCnic(), cnicIssueDateStr, nadraschema.getMobile(), nadraschema.getRecordType(), nadraschema.getNtn(), nadraschema.getPin(), nadraschema.getSubRecords());
    }




    @PutMapping("/resetStatus")
    public ResponseEntity<?> resetStatus(@RequestBody NadraSchema nadraschema) {

        System.out.println("=================================================");
        System.out.println("Received request to update user: " + nadraschema);
        System.out.println("=================================================");

        return nadraService.resetStatus(nadraschema.getId(),nadraschema.getCnic());
    }



    @PostMapping("/getNadraResponse")
    public ResponseEntity<?> getNadraResponse(@RequestBody NadraSchema nadraschema) {
        if (nadraschema == null || nadraschema.getCnic() == null || nadraschema.getCnic().isEmpty()) {
            return ResponseEntity.badRequest().body("CNIC is required.");
        }
        return nadraService.nadraResponse(nadraschema.getCnic());
    }



}
