package com.bookexchange.msusuarios.exception.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemaTest {
    private Problema problema;
    private List<Problema.Campo> campos;
    private Problema.Campo campo;

    @BeforeEach
    void setUp() {
        problema = new Problema();
        campo = new Problema.Campo("Nome test", "Mensagem Test");
        campos = new ArrayList<>();
        campos.add(campo);
    }

    @Test
    void problemaTest() {
        problema.setDatahora(OffsetDateTime.now());
        problema.setTitulo("Test");
        problema.setStatus(500);
        problema.setCampos(campos);
        // Então
        assertEquals(500, problema.getStatus());
        assertNotNull(problema.getDatahora());
        assertFalse(problema.getCampos().isEmpty());
    }

    @Test
    void CampoTest() {
        assertEquals("Nome test", campo.getNome());
        assertEquals("Mensagem Test", campo.getMensagem());
    }
}