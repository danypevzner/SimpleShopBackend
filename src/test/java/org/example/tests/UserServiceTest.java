package org.example.tests;

import org.example.dao.impl.UserDaoImpl;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testUtils.TestObjectGenerator;

import static org.junit.jupiter.api.Assertions.*;



public class UserServiceTest {
    private static UserService service;
    private User testUser;
    private int testUserId;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    @BeforeAll
    static void setupClass(){
        service = new UserService(new UserDaoImpl());
    }

    @BeforeEach
    void setUpTest(){
        testUser = TestObjectGenerator.generateUser();
        testUserId = service.createUser(testUser.getName(),testUser.getEmail(),testUser.getPasswordHash(),testUser.getCreatedAt());
        System.out.println(testUserId);
    }

    @AfterEach
    void tearDown(){
        if (testUserId != -1) {
            try {
                service.removeUser(testUserId);
            } catch (Exception e) {
                // Игнорируем ошибки при очистке — это не критично для теста
                logger.debug("Failed to remove test user (may already be deleted): {}", e.getMessage());
            }
        }

    }

    @Test
    public void testAddUser() {
        assertTrue(testUserId > 0, "User ID should be positive");
    }

    @Test
    public void testRemoveUser() {
        assertTrue(service.removeUser(testUserId),"User showld be succesfully removed");
        assertThrows(IllegalStateException.class,()->service.getUserById(testUserId));
    }

    @Test
    public void testAlterUser(){
        String newEmail = "altered@test.ru";
        testUser.setEmail(newEmail);

        boolean result = service.alterUser(testUserId,testUser.getName(),testUser.getEmail(),testUser.getPasswordHash(),testUser.getCreatedAt());
        assertTrue(result,"Email change successfully finished");
        User updatedUser = service.getUserById(testUserId);
        assertNotNull(updatedUser);
        assertEquals(updatedUser.getEmail(),newEmail);
    }

    @Test
    public void testGetUserByID(){
        User found = service.getUserById(testUserId);
        assertNotNull(found);
    }

    @Test
    public void getAllUsers(){;
        User found = service.getAllUsers().stream().filter(user1 -> user1.getUser_id()==testUserId).findFirst().get();
        assertEquals(found.getName(),testUser.getName());
        assertEquals(found.getEmail(),testUser.getEmail());
    }

    @Test
    public void testGetNonExistentUser() {
        int notExistingId = Integer.MAX_VALUE;
        assertThrows(IllegalStateException.class,()->service.getUserById(notExistingId));
    }

    @Test
    public void testRemoveNonExistentUser() {
        int notExistingId = Integer.MAX_VALUE;
        assertThrows(IllegalStateException.class,()->service.removeUser(notExistingId));
    }

    @Test
    public void testCreateUserWithNullValues() {
        assertThrows(RuntimeException.class, () ->
                service.createUser(null, "email@test.com", "hash", testUser.getCreatedAt())
        );
    }

}
