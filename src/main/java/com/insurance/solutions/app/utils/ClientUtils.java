package com.insurance.solutions.app.utils;

import com.insurance.solutions.app.models.Client;
import com.insurance.solutions.app.resources.ClientResource;

import java.util.ArrayList;
import java.util.List;

import static com.insurance.solutions.app.utils.VehicleUtils.makeVehicles;

public class ClientUtils {
    public static List<ClientResource> makeClients(List<Client> clients, boolean relationship) {
        List<ClientResource> clientsResources = new ArrayList<>();
        for (Client client : clients) clientsResources.add(makeClient(client, relationship));
        return clientsResources;
    }

    public static ClientResource makeClient(Client client, boolean relationship) {
        if (client == null) return null;

        return new ClientResource(
                client.getId(),
                client.getDni(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getMail(),
                client.getInsuranceCompany(),
                relationship ? makeVehicles(new ArrayList<>(client.getVehicles()), false) : new ArrayList<>()
        );
    }
}
