<template>
  <div class="pagina-cadastro">
    <div class="card-cadastro">
      <div class="cabecalho">
        <div>
          <h1>{{ editando ? "Editar máquina" : "Cadastrar máquina" }}</h1>
          <p>Preencha os dados da máquina industrial.</p>
        </div>

        <button class="btn-voltar" @click="voltar">Voltar</button>
      </div>

      <form @submit.prevent="salvarMaquina" class="formulario">
        <div class="campo">
          <label for="codigoIdentificador">Código identificador</label>
          <input
            id="codigoIdentificador"
            v-model="codigoIdentificador"
            type="text"
            placeholder="Ex: CORT-001"
          />
        </div>

        <div class="campo">
          <label for="modelo">Modelo</label>
          <input
            id="modelo"
            v-model="modelo"
            type="text"
            placeholder="Ex: Cortadeira Hidráulica"
          />
        </div>

        <div class="campo">
          <label for="setor">Setor</label>
          <select id="setor" v-model="idSetor">
            <option :value="null">Selecione um setor</option>
            <option
              v-for="setor in setoresStore.setores"
              :key="setor.idSetor"
              :value="setor.idSetor"
            >
              {{ setor.nomeSetor }}
            </option>
          </select>

          <span v-if="setoresStore.loading" class="mensagem-apoio">
            Carregando setores...
          </span>

          <span v-if="setoresStore.error" class="mensagem-erro">
            {{ setoresStore.error }}
          </span>
        </div>

        <div class="campo-checkbox">
          <input id="ativa" v-model="ativa" type="checkbox" />
          <label for="ativa">Máquina ativa</label>
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
            :disabled="maquinaStore.loading"
          >
            {{
              maquinaStore.loading
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
import { useMaquinasStore } from "@/stores/maquinaStore";
import { useSetoresStore } from "@/stores/setorStore";

const router = useRouter();
const route = useRoute();

const maquinaStore = useMaquinasStore();
const setoresStore = useSetoresStore();

const codigoIdentificador = ref("");
const modelo = ref("");
const idSetor = ref(null);
const ativa = ref(true);
const erroFormulario = ref("");

const editando = computed(() => !!route.params.id);

onMounted(async () => {
  await setoresStore.buscarSetores();
  await maquinaStore.buscarMaquinas();

  if (editando.value) {
    const maquina = maquinaStore.buscarMaquinaPorId(Number(route.params.id));

    if (maquina) {
      codigoIdentificador.value = maquina.codigoIdentificador;
      modelo.value = maquina.modelo;
      idSetor.value = maquina.idSetor;
      ativa.value = maquina.ativa;
    }
  }
});

async function salvarMaquina() {
  erroFormulario.value = "";

  if (!codigoIdentificador.value.trim()) {
    erroFormulario.value = "Informe o código identificador da máquina.";
    return;
  }

  if (!modelo.value.trim()) {
    erroFormulario.value = "Informe o modelo da máquina.";
    return;
  }

  if (!idSetor.value) {
    erroFormulario.value = "Selecione o setor da máquina.";
    return;
  }

  const setorSelecionado = setoresStore.setores.find(
    (setor) => setor.idSetor === Number(idSetor.value),
  );

  if (!setorSelecionado) {
    erroFormulario.value = "Setor selecionado não encontrado.";
    return;
  }

  const maquina = {
    codigoIdentificador: codigoIdentificador.value.trim(),
    modelo: modelo.value.trim(),
    ativa: ativa.value,
    idSetor: setorSelecionado.idSetor,
    nomeSetor: setorSelecionado.nomeSetor,
  };

  try {
    if (editando.value) {
      await maquinaStore.atualizarMaquina(Number(route.params.id), maquina);
    } else {
      await maquinaStore.adicionarMaquina(maquina);
    }

    router.push("/maquinas");
  } catch (erro) {
    erroFormulario.value =
      "Não foi possível salvar a máquina. Verifique os dados e tente novamente.";
  }
}

function voltar() {
  router.push("/maquinas");
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

.campo label,
.campo-checkbox label {
  font-weight: 600;
  color: #334155;
}

.campo input,
.campo select {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  font-size: 15px;
  outline: none;
  background: #ffffff;
}

.campo input:focus,
.campo select:focus {
  border-color: #2563eb;
}

.campo-checkbox {
  display: flex;
  align-items: center;
  gap: 10px;
}

.campo-checkbox input {
  width: 18px;
  height: 18px;
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
</style>
