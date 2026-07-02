package com.manutech.ManuTech.config;


import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.model.OrdemServico;
import com.manutech.ManuTech.model.PerfilUsuario;
import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.Setor;
import com.manutech.ManuTech.model.StatusOrdem;
import com.manutech.ManuTech.model.Tecnico;
import com.manutech.ManuTech.model.Usuario;
import com.manutech.ManuTech.repository.MaquinaRepository;
import com.manutech.ManuTech.repository.OrdemServicoRepository;
import com.manutech.ManuTech.repository.SetorRepository;
import com.manutech.ManuTech.repository.TecnicoRepository;
import com.manutech.ManuTech.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;


@Configuration
public class DataLoader {


    @Bean
    CommandLineRunner carregarDadosParaApresentacao(
            SetorRepository setorRepository,
            TecnicoRepository tecnicoRepository,
            MaquinaRepository maquinaRepository,
            OrdemServicoRepository ordemServicoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {


            // SETORES
            Setor producao = buscarOuCriarSetor(setorRepository, "Produção");
            Setor estoque = buscarOuCriarSetor(setorRepository, "Estoque");
            Setor embalagem = buscarOuCriarSetor(setorRepository, "Embalagem");


            // TÉCNICOS
            Tecnico carlos = buscarOuCriarTecnico(
                    tecnicoRepository,
                    "Carlos Souza",
                    "(48) 99999-1001",
                    List.of(producao, embalagem)
            );


            Tecnico ana = buscarOuCriarTecnico(
                    tecnicoRepository,
                    "Ana Pereira",
                    "(48) 99999-1002",
                    List.of(estoque, producao)
            );


            Tecnico joao = buscarOuCriarTecnico(
                    tecnicoRepository,
                    "João Lima",
                    "(48) 99999-1003",
                    List.of(embalagem)
            );


            // MÁQUINAS
            Maquina esteira = buscarOuCriarMaquina(
                    maquinaRepository,
                    "MAQ-001",
                    "Esteira Transportadora",
                    false,
                    producao
            );


            Maquina empilhadeira = buscarOuCriarMaquina(
                    maquinaRepository,
                    "MAQ-002",
                    "Empilhadeira Elétrica",
                    true,
                    estoque
            );


            Maquina seladora = buscarOuCriarMaquina(
                    maquinaRepository,
                    "MAQ-003",
                    "Seladora Automática",
                    true,
                    embalagem
            );


            Maquina compressor = buscarOuCriarMaquina(
                    maquinaRepository,
                    "MAQ-004",
                    "Compressor Industrial",
                    true,
                    producao
            );


            // USUÁRIOS DE ACESSO
            criarUsuarioSeNaoExistir(
                    usuarioRepository,
                    passwordEncoder,
                    "gestor",
                    "123456",
                    PerfilUsuario.GESTOR,
                    null
            );


            criarUsuarioSeNaoExistir(
                    usuarioRepository,
                    passwordEncoder,
                    "tecnico",
                    "123456",
                    PerfilUsuario.TECNICO,
                    carlos
            );


            criarUsuarioSeNaoExistir(
                    usuarioRepository,
                    passwordEncoder,
                    "ana",
                    "123456",
                    PerfilUsuario.TECNICO,
                    ana
            );


            // ORDENS DE SERVIÇO
            criarOrdemSeNaoExistir(
                    ordemServicoRepository,
                    "Vazamento de óleo na esteira",
                    "Foi identificado vazamento próximo ao motor principal da esteira transportadora.",
                    Prioridade.ALTA,
                    StatusOrdem.ANDAMENTO,
                    esteira,
                    carlos
            );


            criarOrdemSeNaoExistir(
                    ordemServicoRepository,
                    "Falha elétrica no painel da empilhadeira",
                    "A empilhadeira apresenta falha intermitente no painel de controle.",
                    Prioridade.CRITICA,
                    StatusOrdem.ABERTA,
                    empilhadeira,
                    null
            );


            criarOrdemSeNaoExistir(
                    ordemServicoRepository,
                    "Sensor desalinhado na seladora",
                    "Sensor de posicionamento da seladora precisa de ajuste preventivo.",
                    Prioridade.BAIXA,
                    StatusOrdem.CANCELADA,
                    seladora,
                    joao
            );


            criarOrdemSeNaoExistir(
                    ordemServicoRepository,
                    "Ruído no compressor industrial",
                    "Compressor apresenta ruído acima do normal durante operação contínua.",
                    Prioridade.MEDIA,
                    StatusOrdem.CONCLUIDA,
                    compressor,
                    ana
            );
        };
    }


    private Setor buscarOuCriarSetor(SetorRepository repository, String nomeSetor) {
        return repository.findByNomeSetorContainingIgnoreCase(nomeSetor)
                .stream()
                .filter(setor -> setor.getNomeSetor().equalsIgnoreCase(nomeSetor))
                .findFirst()
                .orElseGet(() -> {
                    Setor setor = new Setor();
                    setor.setNomeSetor(nomeSetor);
                    return repository.save(setor);
                });
    }


    private Tecnico buscarOuCriarTecnico(
            TecnicoRepository repository,
            String nome,
            String telefone,
            List<Setor> setores
    ) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .filter(tecnico -> tecnico.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseGet(() -> {
                    Tecnico tecnico = new Tecnico();
                    tecnico.setNome(nome);
                    tecnico.setTelefone(telefone);
                    tecnico.setSetoresAtendidos(setores);
                    return repository.save(tecnico);
                });
    }


    private Maquina buscarOuCriarMaquina(
            MaquinaRepository repository,
            String codigoIdentificador,
            String modelo,
            Boolean ativa,
            Setor setor
    ) {
        return repository.findByCodigoIdentificadorContainingIgnoreCase(codigoIdentificador)
                .stream()
                .filter(maquina -> maquina.getCodigoIdentificador().equalsIgnoreCase(codigoIdentificador))
                .findFirst()
                .orElseGet(() -> {
                    Maquina maquina = new Maquina();
                    maquina.setCodigoIdentificador(codigoIdentificador);
                    maquina.setModelo(modelo);
                    maquina.setAtiva(ativa);
                    maquina.setSetor(setor);
                    return repository.save(maquina);
                });
    }


    private void criarUsuarioSeNaoExistir(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder,
            String login,
            String senha,
            PerfilUsuario perfil,
            Tecnico tecnico
    ) {
        if (repository.existsByLogin(login)) {
            return;
        }


        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(perfil);
        usuario.setTecnico(tecnico);


        repository.save(usuario);
    }


    private void criarOrdemSeNaoExistir(
            OrdemServicoRepository repository,
            String titulo,
            String descricao,
            Prioridade prioridade,
            StatusOrdem status,
            Maquina maquina,
            Tecnico tecnico
    ) {
        boolean jaExiste = repository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .anyMatch(ordem -> ordem.getTitulo().equalsIgnoreCase(titulo));


        if (jaExiste) {
            return;
        }


        OrdemServico ordem = new OrdemServico();
        ordem.setTitulo(titulo);
        ordem.setDescricao(descricao);
        ordem.setPrioridade(prioridade);
        ordem.setStatus(status);
        ordem.setMaquina(maquina);
        ordem.setTecnico(tecnico);


        repository.save(ordem);
    }
}

