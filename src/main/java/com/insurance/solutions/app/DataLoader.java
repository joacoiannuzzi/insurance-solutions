package com.insurance.solutions.app;

import com.insurance.solutions.app.models.*;
import com.insurance.solutions.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.insurance.solutions.app.models.ENUM_CATEGORY.CAR;

@Component
public class DataLoader implements ApplicationRunner {
    private final ClientRepository clientRepository;
    private final DrivingProfileRepository drivingProfileRepository;
    private final MonitoringSystemRepository monitoringSystemRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    final static Random random = new Random();

    @Autowired
    public DataLoader(ClientRepository clientRepository, DrivingProfileRepository drivingProfileRepository, MonitoringSystemRepository monitoringSystemRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.drivingProfileRepository = drivingProfileRepository;
        this.monitoringSystemRepository = monitoringSystemRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Stream.of("Sebastian", "Tomas", "Franco", "Jose").forEach(name -> {
            User user = new User(name, name.toLowerCase() + "@mail.com", new BCryptPasswordEncoder().encode("password"));
            userRepository.save(user);
        });

        List<Client> clients = new ArrayList<>();
        for (int i = 1; i < 9; i++)
            clients.add(
                    new Client(
                            "0000000" + i,
                            "Client" + i,
                            "Lastname",
                            "123456789" + i,
                            "client" + i + "@gmail.com"
                    )
            );

        List<Vehicle> vehicles = new ArrayList<>();
        Stream.of("Ka", "Fiesta", "EcoSport", "Puma", "Focus", "Mustang", "Taos", "Amarok", "Polo", "Gol", "T_Cross", "Vento")
                .forEach(name -> {
                    String number = "";
                    if (vehicles.size() > 9) number = "0" + vehicles.size();
                    else number = "00" + vehicles.size();
                    vehicles.add(new Vehicle("AA" + number + "AA", CAR, "Ford", name));
                });

        List<MonitoringSystem> monitoringSystems = new ArrayList<>();
        for (int i = 1; i < 9; i++)
            monitoringSystems.add(new MonitoringSystem("name" + i, "sensor" + i, "monitoringCompany" + 1));

        List<DrivingProfile> drivingProfiles = new ArrayList<>();
        for (int i = 1; i < 9; i++)
            drivingProfiles.add(
                    new DrivingProfile(
                            random.nextDouble(),
                            random.nextDouble(),
                            random.nextDouble(),
                            random.nextDouble(),
                            random.nextDouble(),
                            new Date(random.nextInt()),
                            new Date(random.nextInt())
                    )
            );

        clientRepository.saveAll(clients);
        vehicleRepository.saveAll(vehicles);
        monitoringSystemRepository.saveAll(monitoringSystems);
        drivingProfileRepository.saveAll(drivingProfiles);

        for (int i = 5; i > -1; i--) {
            Client client = clients.get(i);
            Vehicle vehicle = vehicles.get(i);
            DrivingProfile drivingProfile = drivingProfiles.get(i);
            MonitoringSystem monitoringSystem = monitoringSystems.get(i);

            vehicle.setMonitoringSystem(monitoringSystem);
            monitoringSystem.setVehicle(vehicle);
            monitoringSystem.setIsAssigned(true);

            vehicle.addDrivingProfile(drivingProfile);
            drivingProfile.setVehicle(vehicle);

            vehicle.setClient(client);
            client.addVehicle(vehicle);
        }

        clientRepository.saveAll(clients);
        vehicleRepository.saveAll(vehicles);
        monitoringSystemRepository.saveAll(monitoringSystems);
        drivingProfileRepository.saveAll(drivingProfiles);
    }
}
