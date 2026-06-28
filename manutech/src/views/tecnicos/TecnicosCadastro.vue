<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTecnicosStore } from "@/stores/tecnicoStore";

const route = useRoute();
const router = useRouter();
const store = useTecnicosStore();

const nome = ref("");
const telefone = ref("");
const idsSetores = ref([]);

const erro = ref("");
const isEdit = ref(false);

onMounted(async () => {
  await store.buscarTecnicos();

  const id = route.params.id;

  if (id) {
    isEdit.value = true;

    const tecnico = await store.buscarTecnicoPorId(id);

    if (tecnico) {
      nome.value = tecnico.nome;
      telefone.value = tecnico.telefone;

      idsSetores.value = tecnico.setoresAtendidos.map((setor) => setor.idSetor);
    }
  }
});

async function salvar() {
  erro.value = "";

  if (!nome.value.trim()) {
    erro.value = "O nome do técnico é obrigatório.";
    return;
  }

  if (idsSetores.value.length === 0) {
    erro.value = "Selecione pelo menos um setor atendido.";
    return;
  }

  const tecnico = {
    nome: nome.value.trim(),
    telefone: telefone.value.trim(),
    idsSetores: idsSetores.value,
  };

  if (isEdit.value) {
    await store.atualizarTecnico(route.params.id, tecnico);
  } else {
    await store.adicionarTecnico(tecnico);
  }

  router.push("/tecnicos");
}
</script>

<template>
  <div class="container">
    <h1>
      {{ isEdit ? "Atualizar Técnico" : "Cadastro de Técnico" }}
    </h1>

    <form class="formulario">
      <p v-if="erro" class="mensagem-erro">
        {{ erro }}
      </p>

      <input v-model="nome" type="text" placeholder="Nome do técnico" />

      <input v-model="telefone" type="text" placeholder="Telefone" />

      <div class="grupo-setores">
        <label class="titulo-setores"> Setores atendidos </label>

        <label
          v-for="setor in store.setoresDisponiveis"
          :key="setor.idSetor"
          class="checkbox-setor"
        >
          <input v-model="idsSetores" type="checkbox" :value="setor.idSetor" />

          {{ setor.nomeSetor }}
        </label>
      </div>

      <button type="button" @click="salvar">
        {{ isEdit ? "Atualizar" : "Salvar" }}
      </button>

      <button type="button" class="cancelar" @click="router.push('/tecnicos')">
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
  width: 400px;
}

input {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
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

.grupo-setores {
  display: flex;
  flex-direction: column;
  gap: 10px;

  padding: 15px;

  border: 1px solid #ccc;
  border-radius: 8px;

  background: white;
}

.titulo-setores {
  color: #153b7a;
  font-weight: bold;
  margin-bottom: 5px;
}

.checkbox-setor {
  display: flex;
  align-items: center;
  gap: 8px;

  font-size: 1rem;
  cursor: pointer;
}

.checkbox-setor input {
  width: auto;
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
