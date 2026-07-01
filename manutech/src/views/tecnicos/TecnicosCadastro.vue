<template>
  <div class="pagina-cadastro">
    <div class="card-cadastro">
      <div class="cabecalho">
        <div>
          <h1>{{ editando ? "Editar técnico" : "Cadastrar técnico" }}</h1>
          <p>Preencha os dados do técnico e os setores atendidos.</p>
        </div>

        <button class="btn-voltar" @click="voltar">Voltar</button>
      </div>

      <form @submit.prevent="salvarTecnico" class="formulario">
        <div class="campo">
          <label for="nome">Nome</label>
          <input
            id="nome"
            v-model="nome"
            type="text"
            placeholder="Ex: João Silva"
          />
        </div>

        <div class="campo">
          <label for="telefone">Telefone</label>
          <input
            id="telefone"
            v-model="telefone"
            type="text"
            placeholder="Ex: (48) 99999-9999"
          />
        </div>

        <div class="campo">
          <label>Setores atendidos</label>

          <span v-if="setoresStore.loading" class="mensagem-apoio">
            Carregando setores...
          </span>

          <span v-if="setoresStore.error" class="mensagem-erro">
            {{ setoresStore.error }}
          </span>

          <div class="lista-setores">
            <label
              v-for="setor in setoresStore.setores"
              :key="setor.idSetor"
              class="opcao-setor"
            >
              <input
                v-model="idsSetores"
                type="checkbox"
                :value="setor.idSetor"
              />
              <span>{{ setor.nomeSetor }}</span>
            </label>
          </div>
        </div>

        <p v-if="erroFormulario" class="erro-formulario">
          {{ erroFormulario }}
        </p>

        <div class="acoes">
          <button type="button" class="btn-cancelar" @click="voltar">
            Cancelar
          </button>

          <button
            type="submit"
            class="btn-salvar"
            :disabled="tecnicoStore.loading"
          >
            {{
              tecnicoStore.loading
                ? "Salvando..."
                : editando
                  ? "Salvar alterações"
                  : "Cadastrar"
            }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useTecnicosStore } from "@/stores/tecnicoStore";
import { useSetoresStore } from "@/stores/setorStore";

const router = useRouter();
const route = useRoute();

const tecnicoStore = useTecnicosStore();
const setoresStore = useSetoresStore();

const nome = ref("");
const telefone = ref("");
const idsSetores = ref([]);
const erroFormulario = ref("");

const editando = computed(() => !!route.params.id);

onMounted(async () => {
  await setoresStore.buscarSetores();
  await tecnicoStore.buscarTecnicos();

  if (editando.value) {
    const tecnico = tecnicoStore.buscarTecnicoPorId(Number(route.params.id));

    if (tecnico) {
      nome.value = tecnico.nome;
      telefone.value = tecnico.telefone;
      idsSetores.value = tecnico.setoresAtendidos.map((setor) => setor.idSetor);
    }
  }
});

async function salvarTecnico() {
  erroFormulario.value = "";

  if (!nome.value.trim()) {
    erroFormulario.value = "Informe o nome do técnico.";
    return;
  }

  if (!telefone.value.trim()) {
    erroFormulario.value = "Informe o telefone do técnico.";
    return;
  }

  if (idsSetores.value.length === 0) {
    erroFormulario.value = "Selecione pelo menos um setor atendido.";
    return;
  }

  const tecnico = {
    nome: nome.value.trim(),
    telefone: telefone.value.trim(),
    idsSetores: idsSetores.value.map(Number),
  };

  try {
    if (editando.value) {
      await tecnicoStore.atualizarTecnico(Number(route.params.id), tecnico);
    } else {
      await tecnicoStore.adicionarTecnico(tecnico);
    }

    router.push("/tecnicos");
  } catch (erro) {
    erroFormulario.value =
      "Não foi possível salvar o técnico. Verifique os dados e tente novamente.";
  }
}

function voltar() {
  router.push("/tecnicos");
}
</script>

<style scoped>
.pagina-cadastro {
  width: 100%;
  display: flex;
  justify-content: center;
}

.card-cadastro {
  width: 100%;
  max-width: 760px;
  background: #ffffff;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
}

.cabecalho {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 28px;
}

.cabecalho h1 {
  font-size: 28px;
  color: #1e3a8a;
  margin-bottom: 6px;
}

.cabecalho p {
  color: #64748b;
  font-size: 15px;
}

.formulario {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.campo {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.campo label {
  font-weight: 600;
  color: #334155;
}

.campo input {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  font-size: 15px;
  outline: none;
  background: #ffffff;
}

.campo input:focus {
  border-color: #2563eb;
}

.lista-setores {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  margin-top: 6px;
}

.opcao-setor {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #f8fafc;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 12px 14px;
  cursor: pointer;
}

.opcao-setor input {
  width: 18px;
  height: 18px;
}

.opcao-setor span {
  color: #334155;
  font-weight: 600;
}

.mensagem-apoio {
  font-size: 13px;
  color: #64748b;
}

.mensagem-erro,
.erro-formulario {
  font-size: 14px;
  color: #dc2626;
}

.erro-formulario {
  background: #fee2e2;
  padding: 10px 12px;
  border-radius: 8px;
}

.acoes {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}

.btn-voltar,
.btn-cancelar,
.btn-salvar {
  border: none;
  border-radius: 10px;
  padding: 11px 18px;
  font-weight: 600;
  cursor: pointer;
}

.btn-voltar,
.btn-cancelar {
  background: #e2e8f0;
  color: #334155;
}

.btn-salvar {
  background: #1e3a8a;
  color: #ffffff;
}

.btn-salvar:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.btn-voltar:hover,
.btn-cancelar:hover {
  background: #cbd5e1;
}

.btn-salvar:hover:not(:disabled) {
  background: #1d4ed8;
}

@media (max-width: 700px) {
  .cabecalho {
    flex-direction: column;
  }

  .lista-setores {
    grid-template-columns: 1fr;
  }

  .acoes {
    flex-direction: column;
  }
}
</style>
