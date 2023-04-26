package tech.ada.adamon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.adamon.dto.SalvarJogadorDTO;
import tech.ada.adamon.dto.converter.JogadorDtoConverter;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.model.Jogador;
import tech.ada.adamon.repository.JogadorRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class JogadorService {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private AdamonService adamonService;

    private Random rn = new Random();

    private List<String> vidaDoA = new ArrayList<>();
    private List<String> adamonHeal = new ArrayList<>();
    private List<String> adamonVitorioso = new ArrayList<>();

    private Integer round = 0;

    public List<List<String>> batalhar(Long id1, Long id2) {
        vidaDoA.clear();
        adamonHeal.clear();

        return resultado(id1, id2);
    }

    public List<List<String>> resultado(Long id1, Long id2){
        List<List<String>> resultado = new ArrayList<>();
        Adamon aa = adamonService.encontrarAdamonPorId(id1);
        Adamon bb = adamonService.encontrarAdamonPorId(id2);

        Adamon vitorioso = batalhar1(aa,bb);

        adamonVitorioso.add("Round: " + round + " | " + "VENCEDOR ----> " + vitorioso.getNome());

        resultado.add(adamonVitorioso);
        resultado.add(adamonHeal);
        resultado.add(vidaDoA);

        round++;

        return resultado;
    }

    public Adamon batalhar1(Adamon a, Adamon b){
        Integer aLife = a.getVida() * 10;
        Integer bLife = b.getVida() * 10;
    
        Adamon adamonVitorioso = a;

        while(aLife > 0 || bLife > 0){
            bLife -= atack(a);
            aLife -= atack(b);
            aLife += heal(a);
            bLife += heal(b);
            adamonVitorioso = (adamonVitorioso == a) ? b : a;
        }
        return adamonVitorioso;
    }

    public Integer atack(Adamon adamonAtacante){
        Integer ataque;

        if(random() > 1.8){
            ataque = adamonAtacante.getAtaque() + adamonAtacante.getPoder();
            vidaDoA.add(adamonAtacante.getNome() + " - Critical Hit ---> " + ataque.toString());
            return ataque;
        } else {
            ataque = adamonAtacante.getAtaque();
            vidaDoA.add(adamonAtacante.getNome() + " - Ataque ---> " + ataque.toString());
            return adamonAtacante.getAtaque();
        }
    }

    public Integer heal(Adamon adamonHealing){
        Integer heal;

        if(random() > 1.8){
            heal = adamonHealing.getInteligencia();
            adamonHeal.add(adamonHealing.getNome() + " heal ---> " + heal);
            return heal;
        } else {
            heal = 0;
            adamonHeal.add(adamonHealing.getNome() + " heal ---> " + heal);
            return heal;
        }
    }

    public Double random(){
        return rn.nextDouble(2);
    }

    /*
        1 - Implementar preços na classe 'Adamon'
        2 - Implementar método de compra do 'Adamon'
        3 - Um jogador só pode ter no máximo 6 adamons em sua equipe
        4 - Escrever testes para este método
        5 - Pesquisar como testar um método void com 'Mockito'
     */
    public void comprarAdamon(Jogador jogador, Adamon adamon) {
        List<Adamon> equipeAdamonJogador = jogador.getAdamons();
        BigDecimal saldoAtual = jogador.getSaldo();
        BigDecimal precoAdamon = adamon.obterPreco();

        boolean possuiSaldoSuficiente = saldoAtual.compareTo(precoAdamon) > 0;
        boolean possuiEspacoNaEquipe = equipeAdamonJogador.size() < 6;

        if (possuiEspacoNaEquipe && possuiSaldoSuficiente) {
            equipeAdamonJogador.add(adamon);
            jogador.setSaldo(saldoAtual.subtract(precoAdamon));
            atualizarJogador(jogador, jogador.getId());
        } else if (!possuiSaldoSuficiente) {
            throw new RuntimeException("Não possui saldo suficiente");
        } else if (!possuiEspacoNaEquipe) {
            throw new RuntimeException("Não possui espaço na equipe");
        }
    }

    public void venderAdamon(Jogador comprador, Adamon adamon) {

    }

    public void atualizarJogador(Jogador jogador, Long idJogador) {
        encontrarJogadorPorId(idJogador);
        jogador.setId(idJogador);
        jogadorRepository.save(jogador);
    }

    public Jogador encontrarJogadorPorId(Long idJogador) {
        Optional<Jogador> optionalJogador = jogadorRepository.findById(idJogador);
        return optionalJogador
                .orElseThrow(() -> new RuntimeException("Não encontrado jogador com ID: " + idJogador));
    }

    public Jogador salvarJogador(SalvarJogadorDTO dto) {
        return jogadorRepository.save(JogadorDtoConverter.converterDto(dto));
    }



}
