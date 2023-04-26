package tech.ada.adamon.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.repository.AdamonRepository;
import tech.ada.adamon.util.TestUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdamonServiceTest {

    @InjectMocks
    AdamonService adamonService;

    @InjectMocks
    JogadorService jogadorService;

    @Mock
    AdamonRepository repository;

    private ArrayList<String> adamonHeal;
    private List<String> vidaDoA = new ArrayList<>();
    private List<String> adamonVitorioso = new ArrayList<>();
    private Integer round = 0;
    private Random rn;

    @BeforeEach
    public void setUp() throws SQLException {

        adamonHeal = new ArrayList<String>();

        rn = new Random();
    }

    @Test
    void deveSucessoAoBuscarAdamons() {
        when(repository.findAll()).thenReturn(TestUtils.obterAdamons());
        //execução
        List<Adamon> adamons = adamonService.recuperarTodosAdamons();
        //verificação
        Assertions.assertFalse(adamons.isEmpty());
    }

    @Test
    void deveSucessoAoBuscarAdamonsListaVazia() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        //execução
        List<Adamon> adamons = adamonService.recuperarTodosAdamons();
        //verificação
        assertTrue(adamons.isEmpty());
    }

    @Test
    void deveSucessoAoSalvarNovoAdamon() {
        // cenário
        Adamon adamonASerSalvo = new Adamon();
        adamonASerSalvo.setNome("Alexamon");

        Adamon adamonSalvo = new Adamon();
        adamonSalvo.setId(1l);

        when(repository.save(Mockito.any(Adamon.class))).thenReturn(adamonSalvo);

        Adamon adamon = adamonService.criarNovoAdamon(adamonASerSalvo);

        Assertions.assertNotNull(adamon);
        Assertions.assertEquals(adamonSalvo.getId(), adamon.getId());
    }

    /*
     * ____________________________________________________________________________________
     */
    @Test
    public void resultado(){
        List<List<String>> resultado = new ArrayList<>();
        Adamon aa = TestUtils.obterAdamon1();
        Adamon bb = TestUtils.obterAdamon2();

        Adamon vitorioso = jogadorService.batalhar1(aa,bb);

        adamonVitorioso.add("Round: " + round + " | " + "VENCEDOR ----> " + vitorioso.getNome());

        resultado.add(adamonVitorioso);
        resultado.add(adamonHeal);
        resultado.add(vidaDoA);

        round++;

        assertTrue(round >= 0);
        assertFalse(adamonVitorioso.isEmpty());
        assertFalse(adamonHeal.isEmpty());
        assertFalse(vidaDoA.isEmpty());
    }

    @Test
    public void batalhar1(){
        Adamon a = TestUtils.obterAdamon1();
        Adamon b = TestUtils.obterAdamon2();

        Integer aLife = a.getVida() * 10;
        Integer bLife = b.getVida() * 10;

        assertTrue(aLife >= 0 && aLife <= 1000);
        assertTrue(bLife >= 0 && aLife <= 1000);
    
        Adamon adamonVitorioso = a;

        while(aLife > 0 || bLife > 0){
            bLife -= jogadorService.atack(a);
            aLife -= jogadorService.atack(b);
            aLife += jogadorService.heal(a);
            bLife += jogadorService.heal(b);
            adamonVitorioso = (adamonVitorioso == a) ? b : a;
        }

        assertNotNull(adamonVitorioso);
        assertTrue(adamonVitorioso == a || adamonVitorioso == b);
    }

    @Test
    public void atack() {
        Integer ataque;
        Double random = rn.nextDouble(2);

        Adamon a1 = new Adamon();
        a1.setNome("Alexamon");
        a1.setAtaque(50);

        if (random > 1.8) {
            ataque = a1.getAtaque() + a1.getPoder();
            vidaDoA.add(a1.getNome() + " - Critical Hit ---> " + ataque.toString());
            assertTrue(ataque == (a1.getAtaque() + a1.getPoder()));
        } else {
            ataque = a1.getAtaque();
            vidaDoA.add(a1.getNome() + " - Ataque ---> " + ataque.toString());
            assertTrue(ataque == a1.getAtaque());
        }
        assertFalse(vidaDoA.isEmpty());
    }

    @Test
    public void testHeal() {
        Integer heal;
        Double random = rn.nextDouble(2);

        Adamon a1 = new Adamon();
        a1.setNome("Alexamon");
        a1.setInteligencia(50);

        if (random > 1.8) {
            heal = a1.getInteligencia();
            adamonHeal.add(a1.getNome() + " heal ---> " + heal);
            assertTrue(heal >= 0 && heal <= 100);
        } else {
            heal = 0;
            adamonHeal.add(a1.getNome() + " heal ---> " + heal);
            assertTrue(heal == 0);
        }
        assertFalse(adamonHeal.isEmpty());
    }

    @Test
    void testRandom() {
        for (int i = 0; i < 10; i++) {
            Double result = rn.nextDouble(2);
            Assertions.assertTrue(result >= 0 && result <= 2, "Valor deve estar entre 1 e 2");
        }
    }
}
