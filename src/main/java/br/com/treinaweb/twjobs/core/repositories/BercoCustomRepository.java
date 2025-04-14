package br.com.treinaweb.twjobs.core.repositories;


import br.com.treinaweb.twjobs.core.models.Berco;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BercoCustomRepository {

    private final EntityManager em;

    public BercoCustomRepository(EntityManager em) {
        this.em = em;
    }


//    id
//    imo
//    status
//    nome

    public List<Berco> bercosCustom(Long id, String categoria, String nome){

        String query = "select A from Berco as A";
        String condicao = " where ";

        if(id!=null){

            query += condicao + "A.id = :id";
            condicao = " and ";
        }
        if(categoria!=null){
            query += condicao + "A.categoria = :categoria";
            condicao = " and ";
        }
        if(nome!=null){

            query += condicao + "A.nome = :nome";
            condicao = " and ";
        }


        var q = em.createQuery(query, Berco.class);

        if(id!=null){

            q.setParameter("id", id);
        }

        if(categoria!=null){

            q.setParameter("categoria", categoria);
        }

        if(nome!=null){

            q.setParameter("nome", nome);
        }


        return q.getResultList();

    }


}
