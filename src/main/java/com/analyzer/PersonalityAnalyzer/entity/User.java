package com.analyzer.PersonalityAnalyzer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User {

    @Id
    private String id;
    private String username;
    private String last_analysis;

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

}
