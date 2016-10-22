package cauchy.application;

import java.util.Random;

public class GCauchy {
	
	private int N; // Neuronas de salida
	private int P; // Neuronas de entrada
	private int K; // N�mero de patrones
	private double[][] w; // Matriz de pesos
	private int[][] S; // Vector con pesos aleatorios
	private double n = 1, umbral = 0.5, inc = 1;

	private double T = Double.MAX_VALUE;
	private double T0;
	private double t = 0;
	private double[] net;
	
	private int[][] pmas;
	private int[][] pmenos;
	
	public void inicializar(int[][] e){
		this.N = e[0].length;
		this.P = this.N + (this.N / 2);
		this.K = e.length;
		w = new double[N+P][N+P];
		S = new int[N+P][K];
		net = new double[N +P];
		
		pmas = new int[N + P][N + P];
		pmenos = new int[N + P][N + P];
		
		this.T0 = (this.N + this.P) * 2;
		Random r = new Random();
		double rangeMin = -1d, rangeMax = 1d;
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j <= i; j++){
				w[i][j] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
				w[j][i] = w[i][j];
			}
		}
                
                
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < K; j++){
				if(i < N){
					S[i][j] = e[i][j];
				}
				else{
					S[i][j] = r.nextInt(2);
				}
			}
		}
		
	}
	
	public void inicializarf(int[][] e){
		this.N = e.length;
		this.P = this.N + (this.N / 2);
		this.K = e[0].length;
		
		S = new int[N+P][K];
		net = new double[N +P];
		Random r = new Random();
		
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < K; j++){
				if(i < N){
					S[i][j] = e[i][j];
				}
				else{
					S[i][j] = r.nextInt(2);
				}
			}
		}
		
	}
	
	public void aprendizaje(){
		double Pnet = 0d;
		double x = 0d;
		double deltawmax = 0d;
		Random r = new Random();
		//net = new double[P];
		do{
		
		for(int k = 0; k < K; k++){
			do{
				for(int oculto = 0; oculto < P; oculto++){
					int i = r.nextInt(P) + N;
					net[i] = 0;
					for(int j = 0; j < N + P; j++){
						net[i] += (double) w[i][j] * (double) S[j][k];
					}
					T = T0 / (1d + t);
					Pnet = (0.5)+(1d/Math.PI)*Math.atan(net[i]/T);
					x = r.nextDouble();
					if(Pnet>= x){
						S[i][k] = 1;
					}else{
						S[i][k] = 0;
					}
				}
				
				t += 0.5;
			}while(T > 0.0001);
			T = Double.MAX_VALUE;
			t = 0;
		}
		
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < N + P; j++){
				int suma = 0;
				for(int k = 0; k < K; k++){
					suma += S[i][k] * S[j][k];
				}
				pmas[i][j] = (1/K) * suma;
			}
		}
		
		net = new double[N +P];
		
		for(int k = 0; k < K; k++){
			do{
				for(int oculto = 0; oculto < P + N; oculto++){
					int i = S[r.nextInt(P+N)][k];
					net[i] = 0;
					for(int j = 0; j < N + P; j++){
						net[i] += (double) w[i][j] * (double) S[j][k];
					}
					T = T0 / (1d + t);
					Pnet = (0.5)+(1d/Math.PI)*Math.atan(net[i]/T);
					x = r.nextDouble();
					if(Pnet>= x){
						S[i][k] = 1;
					}else{
						S[i][k] = 0;
					}
				}
				
				t += 0.5;
			}while(T > 0.0001);
			T = Double.MAX_VALUE;
			t = 0;
		}
		
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < N + P; j++){
				int suma = 0;
				for(int k = 0; k < K; k++){
					suma += S[i][k] * S[j][k];
				}
				pmenos[i][j] = (1/K) * suma;
			}
		}
		
		double deltaw = 0.0;
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < N + P; j++){
				deltaw = n * (pmas[i][j] - pmenos[i][j]);
				w[i][j] += deltaw;
				if(Math.abs(deltaw) > deltawmax){
					deltawmax = Math.abs(deltaw);
				}
			}
		}
		
		}while(deltawmax > umbral);
		
	}
	
	public void funcionamiento(){
		double Pnet = 0d;
		double x = 0d;
		double deltawmax = 0d;
		Random r = new Random();
		
		for(int k = 0; k < K; k++){
			do{
				for(int oculto = 0; oculto < P; oculto++){
					int i = S[r.nextInt(P) + N][k];
					net[i] = 0;
					for(int j = 0; j < N + P; j++){
						net[i] += (double) w[i][j] * (double) S[j][k];
					}
					T = T0 / (1d + t);
					Pnet = (0.5)+(1d/Math.PI)*Math.atan(net[i]/T);
					x = r.nextDouble();
					if(Pnet>= x){
						S[i][k] = 1;
					}else{
						S[i][k] = 0;
					}
				}
				
				t += 0.5;
			}while(T > 0.0001);
			T = Double.MAX_VALUE;
			t = 0;
		}
		
		for(int k = 0; k < K; k++){
			for(int i = 0; i < N; i++){
				System.out.print(S[i][k] + " ");
			}
			System.out.println();
		}
	}
}
