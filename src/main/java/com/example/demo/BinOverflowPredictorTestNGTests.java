package com.example.demo;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.impl.*;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Listeners(TestResultListener.class)
public class BinOverflowPredictorTestNGTests {

    private BinRepository binRepository;
    private ZoneRepository zoneRepository;
    private BinServiceImpl binService;
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService userDetailsService;

    @BeforeClass
    public void setUp() {
        binRepository = Mockito.mock(BinRepository.class);
        zoneRepository = Mockito.mock(ZoneRepository.class);
        binService = new BinServiceImpl(binRepository, zoneRepository);
        jwtTokenProvider = new JwtTokenProvider("VerySecretKeyForJwtDemo1234567890");
        userDetailsService = new CustomUserDetailsService();
    }

    private void setId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set id via reflection", e);
        }
    }

    @Test(priority = 1, groups = "servlet")
    public void testServlet_likeContextLoadsForBinService() {
        Assert.assertNotNull(binService);
    }

    @Test(priority = 2, groups = "servlet")
    public void testServlet_likeCreateBin() {
        Zone zone = new Zone();
        setId(zone, 1L);
        zone.setActive(true);
        zone.setZoneName("Downtown");

        Bin bin = new Bin();
        bin.setIdentifier("BIN-001");
        bin.setCapacityLiters(100.0);
        bin.setZone(zone);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        when(binRepository.save(any(Bin.class))).thenAnswer(inv -> {
            Bin b = inv.getArgument(0);
            if (b.getId() == null) {
                setId(b, 100L);
            }
            return b;
        });

        Bin created = binService.createBin(bin);
        Assert.assertEquals(created.getIdentifier(), "BIN-001");
    }

    @Test(priority = 3, groups = "security")
    public void testSecurity_GenerateJwtToken() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("admin@city.com", "admin123", Collections.emptyList());
        String token = jwtTokenProvider.generateToken(auth, 1L, "ADMIN", "admin@city.com");
        Assert.assertNotNull(token);
    }

    @Test(priority = 4, groups = "security")
    public void testSecurity_UserDetailsServiceDefaultAdmin() {
        CustomUserDetailsService.DemoUser user = userDetailsService.getByEmail("admin@city.com");
        Assert.assertEquals(user.getRole(), "ADMIN");
    }
}
