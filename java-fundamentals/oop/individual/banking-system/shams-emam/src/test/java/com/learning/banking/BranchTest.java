package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    private Branch branch;

    @BeforeEach
    void setUp() {

        branch = new Branch(
                "BR001",
                "Maadi Branch",
                "Maadi, Cairo",
                "01012345678",
                "maadi@shamsbank.com",
                "Ahmed Ali"
        );
    }

    // ===============Constructor Tests===============


    @Test
    void constructorShouldCreateBranchSuccessfully() {

        assertEquals("BR001", branch.getBranchId());
        assertEquals("Maadi Branch", branch.getName());
        assertEquals("Maadi, Cairo", branch.getAddress());
        assertEquals("01012345678", branch.getPhone());
        assertEquals("maadi@shamsbank.com", branch.getEmail());
        assertEquals("Ahmed Ali", branch.getManager());
    }

    @Test
    void constructorShouldRejectNullBranchId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        null,
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankBranchId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "   ",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectBranchIdContainingSpaces() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR 001",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectNullName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        null,
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "   ",
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectNullAddress() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        null,
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankAddress() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "   ",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectNullPhone() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        null,
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankPhone() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "   ",
                        "maadi@shamsbank.com",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectNullEmail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        null,
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankEmail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        "   ",
                        "Ahmed Ali"
                )
        );
    }

    @Test
    void constructorShouldRejectNullManager() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        null
                )
        );
    }

    @Test
    void constructorShouldRejectBlankManager() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Branch(
                        "BR002",
                        "Maadi Branch",
                        "Maadi, Cairo",
                        "01012345678",
                        "maadi@shamsbank.com",
                        "   "
                )
        );
    }

    // ============Manager Tests==============

    @Test
    void changeManagerShouldUpdateManager() {

        branch.changeManager("Mohamed Hassan");

        assertEquals(
                "Mohamed Hassan",
                branch.getManager()
        );
    }

    @Test
    void changeManagerShouldRejectNullManager() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branch.changeManager(null)
        );

        assertEquals(
                "Ahmed Ali",
                branch.getManager()
        );
    }

    @Test
    void changeManagerShouldRejectBlankManager() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branch.changeManager("   ")
        );

        assertEquals(
                "Ahmed Ali",
                branch.getManager()
        );
    }

    // ==============State Tests=============
    @Test
    void changingManagerShouldNotChangeOtherBranchInformation() {

        branch.changeManager("Mohamed Hassan");

        assertEquals("BR001", branch.getBranchId());
        assertEquals("Maadi Branch", branch.getName());
        assertEquals("Maadi, Cairo", branch.getAddress());
        assertEquals("01012345678", branch.getPhone());
        assertEquals("maadi@shamsbank.com", branch.getEmail());

        assertEquals(
                "Mohamed Hassan",
                branch.getManager()
        );
    }
}