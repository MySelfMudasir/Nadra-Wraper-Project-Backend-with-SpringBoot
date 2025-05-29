package com.NadraWraper.Service;

import com.NadraWraper.Model.entity.NadraSchema;
import com.NadraWraper.Repository.NadraRepository;
import com.NadraWraper.Repository.UserRegistrationRepository;
import com.NadraWraper.Repository.VerisysLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.NadraWraper.Utility.AESEncryption;
import com.NadraWraper.Utility.AESKeyProvider;

import java.sql.Clob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.Clob;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import static org.springframework.http.ResponseEntity.*;

@Service
public class NadraService {

    @Autowired
    // Injecting the NadraRepository to save data to DB
    private NadraRepository nadraRepository;
    private VerisysLogsRepository verisysLogsRepository;
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;
    @Autowired
    private AESEncryption aesEncryption;
    @Autowired
    private AESKeyProvider aesKeyProvider;

    public ResponseEntity<String> saveAccount(String email, String accountType, String cnic, String cnicIssueDate, String mobile, String recordType, String ntn, String pin, List<NadraSchema> subRecords) {
        // Create a new NadraSchema object and set the values

        // Get the next ID from the repository
        Long nextId = nadraRepository.getNextManagementId();
        if (nextId == null) nextId = 1L;


        try {
            // Save main user
            NadraSchema mainRecord = new NadraSchema();
            mainRecord.setId(nextId);
            mainRecord.setAccountType(accountType.trim());
            mainRecord.setCnic(cnic.trim());
            mainRecord.setEmail(email.trim());
            mainRecord.setMobile(mobile.trim());
            mainRecord.setStatus("P".trim()); // Use uppercase
            mainRecord.setRecordType(recordType.trim()); // <- make sure this is being called!
            mainRecord.setNtn(ntn.trim()); // <- make sure this is being called!
            String encryptedPin = encryptEachPin(pin);
            mainRecord.setPin(encryptedPin);

            // Convert String date to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate cnicIssueDateConverted = LocalDate.parse(cnicIssueDate, formatter);
            mainRecord.setCnicIssueDate(cnicIssueDateConverted);

            // Save the user to the database
            denormalizeUserSchema(mainRecord);
            nadraRepository.save(mainRecord);

            // Now save subRecords
            if (subRecords != null) {
                for (NadraSchema sub : subRecords) {
                    Long subNextId = nadraRepository.getNextManagementId(); // get a new ID
                    sub.setId(subNextId); // set the ID
                    sub.setSubAccountId(nextId); // set the ID
                    mainRecord.setRecordType(recordType.trim()); // <- check if recordType is null
                    mainRecord.setNtn(ntn.trim()); // <- check if recordType is null
                    mainRecord.setCnic(cnic);
                    sub.setPin(mainRecord.getPin());
                    sub.setStatus("P"); // Also use uppercase
                    mainRecord.setCnicIssueDate(cnicIssueDateConverted);
                    nadraRepository.save(sub);
                }
            }
        } catch (Exception e) {
            // Return a 500 response if there was an error saving
            return new ResponseEntity<>("Error: Account addition failed. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return a success response if everything went well
        return new ResponseEntity<>("Account added successfully", HttpStatus.OK);
    }




    public List<NadraSchema> findAllUsers() {
        List<NadraSchema> primaryRecords = nadraRepository.findByRecordTypeNative("primary");

        // Process each primary record and its sub-records
        for (NadraSchema primary : primaryRecords) {
            List<NadraSchema> subRecords = nadraRepository.findBySubAccountIdNative(primary.getId());

            // Set sub-records for the primary record
            primary.setSubRecords(subRecords);

            // Normalize primary + sub-records (after setting subRecords)
            normalizeUserSchema(primary);

            // Decrypt PIN for primary and sub-records
            decryptEachPin(primary);  // Primary
            for (NadraSchema subRecord : subRecords) {
                decryptEachPin(subRecord);  // Sub-records
            }
        }
        return primaryRecords;
    }



    public String encryptEachPin(String pin) {
        try {
            String plainPin = pin;

            System.out.println(plainPin);

            // Check if the PIN is null or empty
            if (plainPin == null || plainPin.isEmpty()) {
                return "PIN is not set for this user";
            }

            // Encrypt the PIN using AES encryption
            String encryptedPin = aesEncryption.encrypt(
                    plainPin,
                    aesKeyProvider.getSecretKey(),
                    aesKeyProvider.getIv()
            );

            // Set the encrypted PIN back into the object
//            pin.setPin(encryptedPin); // This ensures the encrypted PIN is set on the object
            return encryptedPin; // Return the encrypted PIN
        } catch (Exception e) {
            e.printStackTrace(); // or use proper logging
            return "Error occurred while encrypting PIN: " + e.getMessage();
        }
    }



    public String decryptEachPin(NadraSchema pin) {
        try {
            String encryptedPin = pin.getPin();

            // Check if the PIN is null or empty
            if (encryptedPin == null || encryptedPin.isEmpty()) {
                pin.setPin("PIN is not set for this user");
                return encryptedPin;
            }

            // Decrypt the PIN using AES encryption
            String decryptedPin = aesEncryption.decrypt(
                    encryptedPin,
                    aesKeyProvider.getSecretKey(),
                    aesKeyProvider.getIv()
            );

            // Set the decrypted PIN back into the object
            pin.setPin(decryptedPin); // This ensures the decrypted PIN is set on the object
            return decryptedPin; // Return the decrypted PIN
        } catch (Exception e) {
            e.printStackTrace(); // or use proper logging
            return "Error occurred while decrypting PIN: " + e.getMessage();
        }
    }



    private void denormalizeUserSchema(NadraSchema user) {
        // Denormalize accountType
        if (user.getAccountType() != null) {
            switch (user.getAccountType().toLowerCase()) {
                case "individual":
                    user.setAccountType("i");
                    break;
                case "corporate":
                    user.setAccountType("c");
                    break;
            }
        }

        // Denormalize status
        if (user.getStatus() != null) {
            switch (user.getStatus().toLowerCase()) {
                case "pending":
                    user.setStatus("P");
                    break;
                case "active":
                    user.setStatus("A");
                    break;
            }
        }

        // Denormalize sub-records recursively
        if (user.getSubRecords() != null) {
            for (NadraSchema sub : user.getSubRecords()) {
                denormalizeUserSchema(sub);
            }
        }
    }



    private void normalizeUserSchema(NadraSchema user) {
        // Normalize accountType
        if (user.getAccountType() != null) {
            switch (user.getAccountType().toLowerCase()) {
                case "i":
                    user.setAccountType("individual");
                    break;
                case "c":
                    user.setAccountType("corporate");
                    break;
            }
        }

        // Normalize status
        if (user.getStatus() != null) {
            switch (user.getStatus().toUpperCase()) {
                case "P":
                    user.setStatus("pending");
                    break;
                case "A":
                    user.setStatus("active");
                    break;
            }
        }

        // Normalize sub-records recursively
        if (user.getSubRecords() != null) {
            for (NadraSchema sub : user.getSubRecords()) {
                normalizeUserSchema(sub);
            }
        }
    }








    public Optional<NadraSchema> findUserById(Long id) {
        return nadraRepository.findById(id); // Assuming nadraRepository extends JpaRepository
    }



    public ResponseEntity<String> updateAccount(Long id, String email, String accountType, String cnic, String cnicIssueDateStr, String mobile, String recordType, String ntn, String pin, List<NadraSchema> subRecords) {
        try {
            LocalDate cnicIssueDate = LocalDate.parse(cnicIssueDateStr);


            NadraSchema mainRecord = new NadraSchema();
            mainRecord.setId(id);
            mainRecord.setAccountType(accountType.trim());
            mainRecord.setCnic(cnic.trim());
            mainRecord.setEmail(email.trim());
            mainRecord.setMobile(mobile.trim());
            mainRecord.setStatus("P".trim()); // Use uppercase
            mainRecord.setRecordType(recordType.trim()); // <- make sure this is being called!
            mainRecord.setNtn(ntn.trim()); // <- make sure this is being called!
            String encryptedPin = encryptEachPin(pin);
            mainRecord.setPin(encryptedPin);

            denormalizeUserSchema(mainRecord);
            encryptEachPin(pin);

            // 1. Update parent record
            int updated = nadraRepository.updateUserById(
                    mainRecord.getId(),
                    mainRecord.getEmail(),
                    mainRecord.getAccountType(),
                    mainRecord.getCnic(),
                    mainRecord.getCnicIssueDate(),
                    mainRecord.getMobile(),
                    mainRecord.getRecordType(),
                    mainRecord.getNtn(),
                    mainRecord.getPin()
            );
            if (updated == 0) {
                return badRequest().body("Parent record not found.");
            }

            // 2. Update sub-records
            if (subRecords != null && !subRecords.isEmpty()) {
                for (NadraSchema sub : subRecords) {
                    LocalDate subCnicIssueDate = sub.getCnicIssueDate();

                    // Try to update the existing sub-record
                    int subUpdated = nadraRepository.updateSubRecordById(
                            sub.getId(),
                            sub.getCnic(),
                            subCnicIssueDate,
                            sub.getMobile(),
                            sub.getRecordType(),
                            sub.getNtn(),
                            sub.getPin()
                    );

                    // If update fails, optional: insert new sub-record
                    if (subUpdated == 0) {
                        Long subNextId = nadraRepository.getNextManagementId(); // Generate new ID
                        sub.setId(subNextId);
                        sub.setSubAccountId(id); // Link to parent
                        sub.setPin(mainRecord.getPin()); // If applicable
                        sub.setStatus("P"); // Ensure default status
                        denormalizeUserSchema(sub);
                        nadraRepository.save(sub);
                    }

                }
            }

            return ok("Parent and sub-records updated");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }





    public ResponseEntity<String> resetStatus(Long id, String cnic) {
        try {
            // 1. Update parent record
            int updated = nadraRepository.resetStatusByCnic("P", cnic); // Use a valid value like "A"
            if (updated == 0) {
                return ok("cnic not found");
            }
            return ok("status updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    //    verisysLogsRepository
    public ResponseEntity<?> nadraResponse(String cnic) {
        List<Object[]> result = nadraRepository.findNadraResponseByCnic(cnic);

        if (result == null || result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found for CNIC: " + cnic);
        }

        Object[] row = result.get(0);

        Timestamp timestamp = (Timestamp) row[0]; // RESPONSE_TIMESTAMP
        Clob clob = (Clob) row[1];                // NADRA_VERIFY_CITIZEN_RESPONSE

        // Convert CLOB to String
        String nadraResponse;
        try {
            nadraResponse = clob.getSubString(1, (int) clob.length());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read NADRA response.");
        }

        // Format timestamp
        String formattedTimestamp = timestamp.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Object> response = new HashMap<>();
        response.put("nadraResponse", nadraResponse);
        response.put("timestamp", formattedTimestamp);

        return ResponseEntity.ok(response);
    }




    public void deleteUser(Long id) {
        nadraRepository.deleteById(id);
    }



}
