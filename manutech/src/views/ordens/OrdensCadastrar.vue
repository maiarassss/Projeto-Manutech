<template>
  <div class="pagina-cadastro">
    <div class="card-cadastro">
      <div class="cabecalho">
        <div>
          <h1>{{ editando ? "Editar ordem" : "Cadastrar ordem" }}</h1>
          <p>Preencha os dados da ordem de serviço.</p>
        </div>

        <button class="btn-voltar" @click="voltar">Voltar</button>
      </div>

      <form @submit.prevent="salvarOrdem" class="formulario">
        <div class="campo">
          <label for="titulo">Título</label>
          <input
            id="titulo"
            v-model="titulo"
            type="text"
            placeholder="Ex: Manutenção preventiva"
          />
        </div>

        <div class="campo">
          <label for="descricao">Descrição</label>
          <textarea
            id="descricao"
            v-model="descricao"
            rows="4"
            placeholder="Descreva o problema ou serviço necessário..."
          ></textarea>
        </div>

        <div class="linha-campos">
          <div class="campo">
            <label for="prioridade">Prioridade</label>
            <select id="prioridade" v-model="prioridade">
              <option value="BAIXA">Baixa</option>
              <option value="MEDIA">Média</option>
              <option value="ALTA">Alta</option>
              <option value="CRITICA">Crítica</option>
            </select>
          </div>

          <div class="campo">
            <label for="status">Status</label>
            <select id="status" v-model="status">
              <option value="ABERTA">Aberta</option>
              <option value="ANDAMENTO">Em andamento</option>
              <option value="CONCLUIDA">Concluída</option>
              <option value="CANCELADA">Cancelada</option>
            </select>
          </div>
        </div>

        <div class="campo">
          <label for="maquina">Máquina</label>
          <select id="maquina" v-model="idMaquina">
            <option :value="null">Selecione uma máquina</option>
            <option
              v-for="maquina in maquinasDisponiveis"
              :key="maquina.idMaquina"
              :value="maquina.idMaquina"
            >
              {{ maquina.codigoIdentificador }} - {{ maquina.modelo }}
            </option>
          </select>

          <span v-if="maquinaStore.loading" class="mensagem-apoio">
            Carregando máquinas...
          </span>

          <span v-if="maquinaStore.error" class="mensagem-erro">
            {{ maquinaStore.error }}
          </span>
        </div>

        <div class="campo">
          <label for="tecnico">Técnico responsável</label>
          <select id="tecnico" v-model="idTecnico">
            <option :value="null">Técnico ainda não atribuído</option>
            <option
              v-for="tecnico in tecnicoStore.tecnicos"
              :key="tecnico.idTecnico"
              :value="tecnico.idTecnico"
            >
              {{ tecnico.nome }}
            </option>
          </select>

          <span v-if="tecnicoStore.loading" class="mensagem-apoio">
            Carregando técnicos...
          </span>

          <span v-if="tecnicoStore.error" class="mensagem-erro">
            {{ tecnicoStore.error }}
          </span>
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
            :disabled="ordemStore.loading"
          >
            {{
              ordemStore.loading
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
import { useOrdensStore } from "@/stores/ordemStore";
import { useMaquinasStore } from "@/stores/maquinaStore";
import { useTecnicosStore } from "@/stores/tecnicoStore";

const router = useRouter();
const route = useRoute();

const ordemStore = useOrdensStore();
const maquinaStore = useMaquinasStore();
const tecnicoStore = useTecnicosStore();

const titulo = ref("");
const descricao = ref("");
const prioridade = ref("BAIXA");
const status = ref("ABERTA");
const idMaquina = ref(null);
const idTecnico = ref(null);
const erroFormulario = ref("");

const editando = computed(() => !!route.params.id);

const maquinasDisponiveis = computed(() => {
  return maquinaStore.maquinas.filter((maquina) => maquina.ativa);
});

onMounted(async () => {
  await maquinaStore.buscarMaquinas();
  await tecnicoStore.buscarTecnicos();
  await ordemStore.buscarOrdens();

  if (editando.value) {
    const ordem = ordemStore.buscarOrdemPorId(Number(route.params.id));

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

async function salvarOrdem() {
  erroFormulario.value = "";

  if (!titulo.value.trim()) {
    erroFormulario.value = "Informe o título da ordem.";
    return;
  }

  if (!descricao.value.trim()) {
    erroFormulario.value = "Informe a descrição da ordem.";
    return;
  }

  if (!idMaquina.value) {
    erroFormulario.value = "Selecione uma máquina.";
    return;
  }

  const ordem = {
    titulo: titulo.value.trim(),
    descricao: descricao.value.trim(),
    prioridade: prioridade.value,
    status: status.value,
    idMaquina: Number(idMaquina.value),
    idTecnico: idTecnico.value ? Number(idTecnico.value) : null,
  };

  try {
    if (editando.value) {
      await ordemStore.atualizarOrdem(Number(route.params.id), ordem);
    } else {
      await ordemStore.adicionarOrdem(ordem);
    }

    router.push("/ordens");
  } catch (erro) {
    erroFormulario.value =
      "Não foi possível salvar a ordem. Verifique os dados e tente novamente.";
  }
}

function voltar() {
  router.push("/ordens");
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

.linha-campos {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.campo label {
  font-weight: 600;
  color: #334155;
}

.campo input,
.campo select,
.campo textarea {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  font-size: 15px;
  outline: none;
  background: #ffffff;
  resize: vertical;
}

.campo input:focus,
.campo select:focus,
.campo textarea:focus {
  border-color: #2563eb;
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

  .linha-campos {
    grid-template-columns: 1fr;
  }

  .acoes {
    flex-direction: column;
  }
}
</style>
