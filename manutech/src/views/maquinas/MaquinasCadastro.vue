<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useMaquinasStore } from "@/stores/maquinaStore";

const route = useRoute();
const router = useRouter();
const store = useMaquinasStore();

const codigoIdentificador = ref("");
const modelo = ref("");
const setor = ref("");
const ativa = ref(true);
const erro = ref("");

const isEdit = ref(false);

onMounted(async () => {
  await store.buscarMaquinas();

  const id = route.params.id;

  if (id) {
    isEdit.value = true;

    const maquina = await store.buscarMaquinaPorId(id);

    if (maquina) {
      codigoIdentificador.value = maquina.codigoIdentificador;
      modelo.value = maquina.modelo;
      setor.value = maquina.nomeSetor;
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

  if (!setor.value) {
    erro.value = "Selecione o setor da máquina.";
    return;
  }

  const codigoJaExiste = store.maquinas.some((maquina) => {
    return (
      maquina.codigoIdentificador.toLowerCase() ===
        codigoIdentificador.value.trim().toLowerCase() &&
      maquina.idMaquina != route.params.id
    );
  });

  if (codigoJaExiste) {
    erro.value = "Já existe uma máquina cadastrada com esse código.";
    return;
  }

  const maquina = {
    codigoIdentificador: codigoIdentificador.value.trim(),
    modelo: modelo.value.trim(),
    nomeSetor: setor.value,
    ativa: ativa.value,
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
      {{ isEdit ? "Atualizar Máquina" : "Cadastro de Máquinas" }}
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

      <select v-model="setor">
        <option value="">Selecione o setor</option>

        <option>Corte</option>

        <option>Costura</option>

        <option>Montagem</option>

        <option>Acabamento</option>
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
.formulario {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 400px;
}

input,
select {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

button {
  background: #173f82;
  color: white;
  padding: 12px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
}

.cancelar {
  background: #777;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 30px;
}

h1 {
  color: #153b7a;
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
