package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerTypeConverter implements DynamoDBTypeConverter<List<String>, List<Customer>> {

    @Override
    public List<String> convert(List<Customer> object) {

        return object.stream().map(n -> String.format("%s x %s x %s", n.getId(), n.getName(), n.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> unconvert(List<String> object) {

        return object.stream().map(n -> {
                    Customer customer = new Customer();
                    String[] data = n.split("x");
                    customer.setId(data[0].trim());
                    customer.setName(data[1].trim());
                    customer.setEmail(data[2].trim());
                    return customer;
                })
                .collect(Collectors.toList());
    }
}
