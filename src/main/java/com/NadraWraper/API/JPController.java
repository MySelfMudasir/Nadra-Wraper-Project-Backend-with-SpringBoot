package com.NadraWraper.API;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jp")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JPController {
}
