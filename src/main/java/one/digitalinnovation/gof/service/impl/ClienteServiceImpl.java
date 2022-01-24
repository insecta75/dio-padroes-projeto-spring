package one.digitalinnovation.gof.service.impl;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired//Injetar os componentes do Spring
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll(); //Busca todos os clientes
    }
    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id); //Assume que sempre tem um cliente
        return cliente.get();
    }
    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }
    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);//Buscar Cliente pelo id, caso exista
        if(clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }
    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep(); //Verificar se o Endereco do Cliente existe (CEP)
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco; //Caso nao existir, integrar com o ViaCep e persistir o retorno
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);//Inserir Cliente, vinculando o Endereco (novo ou existente)
    }
}