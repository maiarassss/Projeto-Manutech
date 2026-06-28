import { ref } from "vue";
import { defineStore } from "pinia";

export const useTecnicosStore = defineStore("tecnicos", () => {
  const setoresDisponiveis = ref([
    {
      idSetor: 1,
      nomeSetor: "Corte",
    },
    {
      idSetor: 2,
      nomeSetor: "Costura",
    },
    {
      idSetor: 3,
      nomeSetor: "Montagem",
    },
    {
      idSetor: 4,
      nomeSetor: "Acabamento",
    },
  ]);

  const tecnicos = ref([
    {
      idTecnico: 1,
      nome: "João Silva",
      telefone: "(48) 99999-1111",
      setoresAtendidos: [
        {
          idSetor: 1,
          nomeSetor: "Corte",
        },
        {
          idSetor: 3,
          nomeSetor: "Montagem",
        },
      ],
    },
    {
      idTecnico: 2,
      nome: "Maria Oliveira",
      telefone: "(48) 99999-2222",
      setoresAtendidos: [
        {
          idSetor: 2,
          nomeSetor: "Costura",
        },
      ],
    },
    {
      idTecnico: 3,
      nome: "Pedro Santos",
      telefone: "(48) 99999-3333",
      setoresAtendidos: [
        {
          idSetor: 4,
          nomeSetor: "Acabamento",
        },
      ],
    },
  ]);

  async function buscarTecnicos() {
    return tecnicos.value;
  }

  async function buscarTecnicoPorId(id) {
    return tecnicos.value.find((tecnico) => tecnico.idTecnico == id);
  }

  async function adicionarTecnico(tecnico) {
    const setoresAtendidos = setoresDisponiveis.value.filter((setor) =>
      tecnico.idsSetores.includes(setor.idSetor),
    );

    const novoTecnico = {
      idTecnico: Date.now(),
      nome: tecnico.nome,
      telefone: tecnico.telefone,
      setoresAtendidos,
    };

    tecnicos.value.push(novoTecnico);

    return novoTecnico;
  }

  async function atualizarTecnico(id, tecnico) {
    const index = tecnicos.value.findIndex((t) => t.idTecnico == id);

    const setoresAtendidos = setoresDisponiveis.value.filter((setor) =>
      tecnico.idsSetores.includes(setor.idSetor),
    );

    if (index !== -1) {
      tecnicos.value[index] = {
        idTecnico: Number(id),
        nome: tecnico.nome,
        telefone: tecnico.telefone,
        setoresAtendidos,
      };
    }

    return tecnicos.value[index];
  }

  async function excluirTecnico(id) {
    tecnicos.value = tecnicos.value.filter(
      (tecnico) => tecnico.idTecnico != id,
    );
  }

  return {
    tecnicos,
    setoresDisponiveis,
    buscarTecnicos,
    buscarTecnicoPorId,
    adicionarTecnico,
    atualizarTecnico,
    excluirTecnico,
  };
});
