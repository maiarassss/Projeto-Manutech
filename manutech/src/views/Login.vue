<template>
  <div class="pagina-login">
    <div class="card-login">
      <div class="area-logo">
        <div class="logo">M</div>
        <h1>MANUTECH</h1>
        <p>Sistema de manutenção industrial</p>
      </div>

      <form class="formulario-login" @submit.prevent="entrar">
        <div class="campo">
          <label for="login">Usuário</label>
          <input
            id="login"
            v-model="login"
            type="text"
            placeholder="Digite seu usuário"
          />
        </div>

        <div class="campo">
          <label for="senha">Senha</label>
          <input
            id="senha"
            v-model="senha"
            type="password"
            placeholder="Digite sua senha"
          />
        </div>

        <p v-if="erro" class="mensagem-erro">
          {{ erro }}
        </p>

        <p v-if="mensagemSucesso" class="mensagem-sucesso">
          {{ mensagemSucesso }}
        </p>

        <button type="submit" class="btn-entrar" :disabled="carregando">
          {{ carregando ? "Entrando..." : "Entrar" }}
        </button>
      </form>

      <div class="rodape-login">
        <span>Login integrado ao backend com Spring Security</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { api } from "@/services/api";
import { useAuthStore } from "@/stores/authStore";

const router = useRouter();
const authStore = useAuthStore();

const login = ref("");
const senha = ref("");
const erro = ref("");
const mensagemSucesso = ref("");
const carregando = ref(false);

async function entrar() {
  erro.value = "";
  mensagemSucesso.value = "";

  if (!login.value.trim()) {
    erro.value = "Informe o usuário.";
    return;
  }

  if (!senha.value.trim()) {
    erro.value = "Informe a senha.";
    return;
  }

  carregando.value = true;

  try {
    const resposta = await api.post("/login", {
      login: login.value.trim(),
      senha: senha.value,
    });

    const usuario = resposta.data;

    authStore.salvarUsuario(usuario);

    mensagemSucesso.value = usuario.mensagem || "Login realizado com sucesso.";

    setTimeout(() => {
      router.push("/");
    }, 600);
  } catch (error) {
    console.error(error);

    if (error.response?.status === 401 || error.response?.status === 403) {
      erro.value = "Usuário ou senha inválidos.";
      return;
    }

    erro.value = "Não foi possível realizar o login. Tente novamente.";
  } finally {
    carregando.value = false;
  }
}
</script>

<style scoped>
.pagina-login {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #1e3a8a, #0f172a);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
}

.card-login {
  width: 100%;
  max-width: 420px;
  background: #ffffff;
  border-radius: 18px;
  padding: 32px;
  box-shadow: 0 20px 45px rgba(15, 23, 42, 0.25);
}

.area-logo {
  text-align: center;
  margin-bottom: 28px;
}

.logo {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: #1e3a8a;
  color: #ffffff;
  font-size: 32px;
  font-weight: 800;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 14px;
}

.area-logo h1 {
  color: #1e3a8a;
  font-size: 30px;
  margin-bottom: 6px;
  letter-spacing: 1px;
}

.area-logo p {
  color: #64748b;
  font-size: 15px;
}

.formulario-login {
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

.mensagem-erro {
  background: #fee2e2;
  color: #b91c1c;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 14px;
}

.mensagem-sucesso {
  background: #dcfce7;
  color: #166534;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 14px;
}

.btn-entrar {
  border: none;
  border-radius: 10px;
  padding: 12px 18px;
  background: #1e3a8a;
  color: #ffffff;
  font-weight: 700;
  font-size: 15px;
  cursor: pointer;
}

.btn-entrar:hover:not(:disabled) {
  background: #1d4ed8;
}

.btn-entrar:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.rodape-login {
  margin-top: 22px;
  text-align: center;
  color: #64748b;
  font-size: 13px;
}
</style>
