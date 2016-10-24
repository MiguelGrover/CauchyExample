package cauchy.application;

import java.util.Random;

public class GCauchy {
	
	private int N; // Neuronas de salida
	private int P; // Neuronas de entrada
	private int K; // N�mero de patrones
	private double[][] w; // Matriz de pesos
	private int[][] S; // Vector con pesos aleatorios
	private double n = 1, umbral = 0.2, inc = 1, TF = 1;

	private double T = Double.MAX_VALUE;
	private double T0;
	private double t = 0;
	private double[] net;
	
	private double[][] pmas;
	private double[][] pmenos;
        private int[][] entradas;
        
        double errorMaximo = 0;
	
	public void inicializar(int[][] e){
		this.N = e[0].length;
		this.P = this.N * 2;
		this.K = e.length;
                this.entradas = e;
                this.umbral = 1d / this.K;
		w = new double[N+P][N+P];
		S = new int[K][N+P];
		net = new double[N +P];
                errorMaximo=(((N+P)*(N+P))/100.0)*1;
		
		pmas = new double[N + P][N + P];
		pmenos = new double[N + P][N + P];
		
		this.T0 = (this.N + this.P) * 4;
                this.TF = 20;
                //this.T0 = 70;
                //this.TF = 100;
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
					S[j][i] = e[j][i];
				}
				else{
					S[j][i] = r.nextInt(2);
				}
			}
		}
		
	}
	
	public void inicializarf(int[][] e){
		this.K = e.length;
		
		S = new int[K][N+P];
		net = new double[N +P];
		Random r = new Random();
		
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < K; j++){
				if(i < N){
					S[j][i] = e[j][i];
				}
				else{
					S[j][i] = r.nextInt(2);
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
						net[i] += (double) w[i][j] * (double) S[k][j];
					}
					T = T0 / (1d + t);
					Pnet = (0.5)+(1.0/Math.PI)*Math.atan(net[i]/T);
					x = r.nextDouble();
					if(Pnet>= x){
						S[k][i] = 1;
					}else{
						S[k][i] = 0;
					}
				}
				
				t += inc;
			}while(T > TF);
			T = Double.MAX_VALUE;
			t = 0;
		}
		
                
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < N + P; j++){
				int suma = 0;
				for(int k = 0; k < K; k++){
					suma += S[k][i] * S[k][j];
				}
				pmas[i][j] = (1.0/(double)K) * suma;
                                
                                
			}
                        
		}
                    
		net = new double[N +P];
		
		for(int k = 0; k < K; k++){
			do{
				for(int oculto = 0; oculto < P + N; oculto++){
					int i = r.nextInt(P+N);
					net[i] = 0;
					for(int j = 0; j < N + P; j++){
						net[i] += (double) w[i][j] * (double) S[k][j];
					}
					T = T0 / (1d + t);
					Pnet = (0.5)+(1d/Math.PI)*Math.atan(net[i]/T);
					x = r.nextDouble();
					if(Pnet>= x){
						S[k][i] = 1;
					}else{
						S[k][i] = 0;
					}
				}
				
				t += inc;
			}while(T > TF);
			T = Double.MAX_VALUE;
			t = 0;
		}
		
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < N + P; j++){
				int suma = 0;
				for(int k = 0; k < K; k++){
					suma += S[k][i] * S[k][j];
				}
				pmenos[i][j] = (1.0/(double)K) * suma;
			}
		}
                
                for(int i = 0; i < N + P; i++){
			for(int j = 0; j < K; j++){
				if(i < N){
					S[j][i] = entradas[j][i];
				}
			}
		}
		
                double sumatoria = 0;
		double deltaw = 0.0;
                deltawmax = 0.0;
		for(int i = 0; i < N + P; i++){
			for(int j = 0; j < N + P; j++){
				deltaw = n * (pmas[i][j] - pmenos[i][j]);
				w[i][j] += deltaw;
                                sumatoria+=Math.abs(deltaw);
                                //System.out.print(String.format("%.2f",deltaw) + " ");
				if(Math.abs(deltaw) > deltawmax){
					deltawmax = Math.abs(deltaw);
                                        
				}
			}
                        //System.out.println();
		}
                deltawmax = sumatoria;
                
		//System.out.println();
		//}while(deltawmax > umbral);
                    System.out.println(deltawmax+" de "+ errorMaximo);
		}while(deltawmax > errorMaximo);
	}
	
	public int[] funcionamiento(){
		double Pnet = 0d;
		double x = 0d;
		double deltawmax = 0d;
		Random r = new Random();
		
		for(int k = 0; k < K; k++){
			do{
				for(int oculto = 0; oculto < N + P; oculto++){
					int i = r.nextInt(P + N);
					net[i] = 0;
					for(int j = 0; j < N + P; j++){
						net[i] += (double) w[i][j] * (double) S[k][j];
					}
					T = T0 / (1d + t);
					Pnet = (0.5)+(1d/Math.PI)*Math.atan(net[i]/T);
					x = r.nextDouble();
					if(Pnet>= x){
						S[k][i] = 1;
					}else{
						S[k][i] = 0;
					}
				}
				
				t += inc;
			}while(T > TF);
			T = Double.MAX_VALUE;
			t = 0;
		}
		int[] arreglo = new int[N*K];
                int contador = 0;
		for(int k = 0; k < K; k++){
			for(int i = 0; i < N; i++){
				System.out.print(S[k][i] + " ");
                                arreglo[contador] = S[k][i];
                                contador++;
			}
			System.out.println();
		}
                return arreglo;
	}
}
