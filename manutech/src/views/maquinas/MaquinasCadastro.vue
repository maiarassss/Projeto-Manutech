<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMaquinasStore } from "@/stores/maquinaStore";

const route = useRoute();
const router = useRouter();
const store = useMaquinasStore();

const codigoIdentificador = ref("");
const modelo = ref("");
const idSetor = ref(null);
const ativa = ref(true);

const erro = ref("");
const isEdit = ref(false);

const setoresDisponiveis = [
  { idSetor: 1, nomeSetor: "Corte" },
  { idSetor: 2, nomeSetor: "Costura" },
  { idSetor: 3, nomeSetor: "Montagem" },
  { idSetor: 4, nomeSetor: "Acabamento" },
];

onMounted(async () => {
  await store.buscarMaquinas();

  const id = route.params.id;

  if (id) {
    isEdit.value = true;

    const maquina = await store.buscarMaquinaPorId(id);

    if (maquina) {
      codigoIdentificador.value = maquina.codigoIdentificador;
      modelo.value = maquina.modelo;
      idSetor.value = maquina.idSetor;
      ativa.value = maquina.ativa;
    }
  }
});

async function salvar() {
  erro.value = "";

  if (!codigoIdentificador.value.trim()) {
    erro.value = "O código identificador é obrigatório.";
    return;
  }

  if (!modelo.value.trim()) {
    erro.value = "O modelo da máquina é obrigatório.";
    return;
  }

  if (!idSetor.value) {
    erro.value = "O setor é obrigatório.";
    return;
  }

  const codigoDuplicado = store.maquinas.some((maquina) => {
    const mesmoCodigo =
      maquina.codigoIdentificador.toLowerCase() ===
      codigoIdentificador.value.trim().toLowerCase();

    const outraMaquina = maquina.idMaquina != route.params.id;

    return mesmoCodigo && outraMaquina;
  });

  if (codigoDuplicado) {
    erro.value = "Já existe uma máquina cadastrada com esse código.";
    return;
  }

  const setorSelecionado = setoresDisponiveis.find(
    (setor) => setor.idSetor == idSetor.value,
  );

  if (!setorSelecionado) {
    erro.value = "Setor selecionado não encontrado.";
    return;
  }

  const maquina = {
    codigoIdentificador: codigoIdentificador.value.trim(),
    modelo: modelo.value.trim(),
    ativa: ativa.value,
    idSetor: setorSelecionado.idSetor,
    nomeSetor: setorSelecionado.nomeSetor,
  };

  if (isEdit.value) {
    await store.atualizarMaquina(route.params.id, maquina);
  } else {
    await store.adicionarMaquina(maquina);
  }

  router.push("/maquinas");
}
</script>

<template>
  <div class="container">
    <h1>
      {{ isEdit ? "Atualizar Máquina" : "Cadastro de Máquina" }}
    </h1>

    <form class="formulario">
      <p v-if="erro" class="mensagem-erro">
        {{ erro }}
      </p>

      <input
        v-model="codigoIdentificador"
        type="text"
        placeholder="Código identificador"
      />

      <input v-model="modelo" type="text" placeholder="Modelo da máquina" />

      <select v-model.number="idSetor">
        <option :value="null">Selecione o setor</option>

        <option
          v-for="setor in setoresDisponiveis"
          :key="setor.idSetor"
          :value="setor.idSetor"
        >
          {{ setor.nomeSetor }}
        </option>
      </select>

      <select v-model="ativa">
        <option :value="true">Ativa</option>
        <option :value="false">Inativa</option>
      </select>

      <button type="button" @click="salvar">
        {{ isEdit ? "Atualizar" : "Salvar" }}
      </button>

      <button type="button" class="cancelar" @click="router.push('/maquinas')">
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
select {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 1rem;
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
