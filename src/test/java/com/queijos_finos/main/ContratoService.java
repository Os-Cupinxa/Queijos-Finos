package com.queijos_finos.main;

import com.queijos_finos.main.model.Contrato;
import com.queijos_finos.main.model.Propriedade;
import com.queijos_finos.main.repository.ContratoRepository;
import com.queijos_finos.main.repository.PropriedadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepo;

    

    public Contrato saveOrUpdateContrato(Long id, String nome, String dataEmissao, String dataVencimento, Long idPropriedade) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("y-M-d");
        Date dataEmissaoConv = formato.parse(dataEmissao);
        Date dataVencimentoConv = formato.parse(dataVencimento);

        Contrato contrato;
        Propriedade propriedade = new Propriedade();
        propriedade.setIdPropriedade(idPropriedade);

        if (id != null && id != -1) {
            contrato = contratoRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contrato não encontrado com o ID: " + id));
            contrato.setNome(nome);
            contrato.setDataEmissao(dataEmissaoConv);
            contrato.setDataVercimento(dataVencimentoConv);
            contrato.setPropriedade(propriedade);
        } else {
            contrato = new Contrato(nome, dataEmissaoConv, dataVencimentoConv, propriedade);
        }

        contratoRepo.save(contrato);
        return contrato;
    }

    public Long deleteContrato(Long id) {
        contratoRepo.deleteById(id);
        return id;
    }
}
