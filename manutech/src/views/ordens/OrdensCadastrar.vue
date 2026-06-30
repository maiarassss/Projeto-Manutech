<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useOrdensStore } from "@/stores/ordemStore";
import { useMaquinasStore } from "@/stores/maquinaStore";
import { useTecnicosStore } from "@/stores/tecnicoStore";

const route = useRoute();
const router = useRouter();

const ordemStore = useOrdensStore();
const maquinaStore = useMaquinasStore();
const tecnicoStore = useTecnicosStore();

const titulo = ref("");
const descricao = ref("");
const prioridade = ref("MEDIA");
const status = ref("ABERTA");
const idMaquina = ref(null);
const idTecnico = ref(null);

const erro = ref("");
const isEdit = ref(false);

onMounted(async () => {
  await ordemStore.buscarOrdens();
  await maquinaStore.buscarMaquinas();
  await tecnicoStore.buscarTecnicos();

  const id = route.params.id;

  if (id) {
    isEdit.value = true;

    const ordem = await ordemStore.buscarOrdemPorId(id);

    if (ordem) {
      titulo.value = ordem.titulo;
      descricao.value = ordem.descricao;
      prioridade.value = ordem.prioridade;
      status.value = ordem.status;
      idMaquina.value = ordem.idMaquina;
      idTecnico.value = ordem.idTecnico;
    }
  }
});

async function salvar() {
  erro.value = "";

  if (!titulo.value.trim()) {
    erro.value = "O título da ordem é obrigatório.";
    return;
  }

  if (!descricao.value.trim()) {
    erro.value = "A descrição da ordem é obrigatória.";
    return;
  }

  if (!idMaquina.value) {
    erro.value = "Selecione uma máquina.";
    return;
  }

  const maquinaSelecionada = maquinaStore.maquinas.find(
    (maquina) => maquina.idMaquina == idMaquina.value,
  );

  if (!maquinaSelecionada) {
    erro.value = "Máquina selecionada não encontrada.";
    return;
  }

  const tecnicoSelecionado = tecnicoStore.tecnicos.find(
    (tecnico) => tecnico.idTecnico == idTecnico.value,
  );

  const ordem = {
    titulo: titulo.value.trim(),
    descricao: descricao.value.trim(),
    prioridade: prioridade.value,
    status: status.value,

    idMaquina: maquinaSelecionada.idMaquina,
    codigoIdentificador: maquinaSelecionada.codigoIdentificador,
    modelo: maquinaSelecionada.modelo,
    nomeSetor: maquinaSelecionada.nomeSetor,

    idTecnico: tecnicoSelecionado?.idTecnico || null,
    nomeTecnico: tecnicoSelecionado?.nome || "Não atribuído",
  };

  if (isEdit.value) {
    await ordemStore.atualizarOrdem(route.params.id, ordem);
  } else {
    await ordemStore.adicionarOrdem(ordem);
  }

  router.push("/ordens");
}
</script>

<template>
  <div class="container">
    <h1>
      {{ isEdit ? "Atualizar Ordem" : "Cadastro de Ordem" }}
    </h1>

    <form class="formulario">
      <p v-if="erro" class="mensagem-erro">
        {{ erro }}
      </p>

      <input v-model="titulo" type="text" placeholder="Título da ordem" />

      <textarea
        v-model="descricao"
        placeholder="Descrição da manutenção"
      ></textarea>

      <select v-model="prioridade">
        <option value="BAIXA">Baixa</option>
        <option value="MEDIA">Média</option>
        <option value="ALTA">Alta</option>
        <option value="CRITICA">Crítica</option>
      </select>

      <select v-model="status">
        <option value="ABERTA">Aberta</option>
        <option value="EM_ANDAMENTO">Em andamento</option>
        <option value="CONCLUIDA">Concluída</option>
        <option value="CANCELADA">Cancelada</option>
      </select>

      <select v-model.number="idMaquina">
        <option :value="null">Selecione a máquina</option>

        <option
          v-for="maquina in maquinaStore.maquinas"
          :key="maquina.idMaquina"
          :value="maquina.idMaquina"
        >
          {{ maquina.codigoIdentificador }} - {{ maquina.modelo }}
        </option>
      </select>

      <select v-model.number="idTecnico">
        <option :value="null">Técnico ainda não atribuído</option>

        <option
          v-for="tecnico in tecnicoStore.tecnicos"
          :key="tecnico.idTecnico"
          :value="tecnico.idTecnico"
        >
          {{ tecnico.nome }}
        </option>
      </select>

      <button type="button" @click="salvar">
        {{ isEdit ? "Atualizar" : "Salvar" }}
      </button>

      <button type="button" class="cancelar" @click="router.push('/ordens')">
        Cancelar
      </button>
    </form>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 30px;
}

h1 {
  color: #153b7a;
  font-size: 2.6rem;
  margin-bottom: 30px;
}

.formulario {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 450px;
}

input,
select,
textarea {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 1rem;
}

textarea {
  min-height: 120px;
  resize: vertical;
}

button {
  background: #173f82;
  color: white;

  padding: 12px;

  border: none;
  border-radius: 8px;

  font-weight: bold;
  cursor: pointer;

  transition: 0.2s;
}

button:hover {
  background: #123063;
}

.cancelar {
  background: #777;
}

.cancelar:hover {
  background: #555;
}

.mensagem-erro {
  background: #ffd9d9;
  color: #b42318;

  padding: 12px;

  border-radius: 8px;

  font-weight: bold;
  text-align: center;
}
</style>
