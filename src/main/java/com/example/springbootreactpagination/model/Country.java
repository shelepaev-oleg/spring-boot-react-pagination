package com.example.springbootreactpagination.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Country {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "id")
    @JsonProperty
    private Long id;

    /**
     * Краткое имя
     */
    @Column(name = "short_name")
    @JsonProperty
    private String shortName;

    /**
     * Имя
     */
    @Column(name = "name")
    @JsonProperty
    private String name;
}
