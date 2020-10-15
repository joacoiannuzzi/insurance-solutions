package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.models.InsuranceCompany;
import com.insurance.solutions.app.resources.ClientResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.InsuranceCompanyUtils.makeInsuranceCompany;
import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicles;

public class ClientUtils {
    public static List<ClientResource> makeClients(List<Client> clients, boolean relationship) {
        List<ClientResource> clientsResources = new ArrayList<>();
        for (Client client : clients) clientsResources.add(makeClient(client, relationship));
        return clientsResources;
    }

    public static ClientResource makeClient(Client client, boolean relationship) {
        if (client == null) return null;

        InsuranceCompany insuranceCompany = client.getInsuranceCompany();

        return new ClientResource(
                client.getId(),
                client.getDni(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getMail(),
                relationship && insuranceCompany != null ? makeInsuranceCompany(insuranceCompany, false) : null,
                relationship ? makeVehicles(new ArrayList<>(client.getVehicles()), false) : new ArrayList<>()
        );
    }
}
